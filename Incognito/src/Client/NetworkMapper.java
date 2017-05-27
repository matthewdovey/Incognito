package Client;

import Commands.MapCommand;
import Database.Host;
import Database.ResultsDatabase;
import Threads.ICMPThread;
import Threads.PingThread;
import Threads.TCPThread;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//TODO: could implement a heart beat - the ip is pinged every 5 seconds to make sure it is still live - just an idea :)

public class NetworkMapper {

    private HashMap<String, String> liveHosts;
    private Console console;
    private ResultsDatabase results;
    private int timeout;

    NetworkMapper(Console console) {
        liveHosts = new HashMap<>();
        this.console = console;
        timeout = 10000;
        results = new ResultsDatabase();
    }

    NetworkMapper(Console console, int timeout) {
        liveHosts = new HashMap<>();
        this.console = console;
        this.timeout = timeout;
        results = new ResultsDatabase();
    }

    public void map(MapCommand command) {
        ArrayList<ICMPThread> threads = createICMPThreads(command);

        final ExecutorService executor = Executors.newFixedThreadPool(threads.size());

        threads.forEach(x -> executor.submit(x));

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ICMPThread thread : threads) {
            if (thread.liveHosts() > 0) {
                liveHosts.putAll(thread.getHosts());
            }
        }

        console.displayPingResults(liveHosts);

        //Creating host objects from the results of the scan
        int index = 0;
        Host[] hosts = new Host[liveHosts.size()];
        Iterator it = liveHosts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry device = (Map.Entry)it.next();
            hosts[index] = new Host((String) device.getKey(), (String) device.getValue(),"Windows");
            index ++;
        }
        updateDatabase(hosts);
    }

    private String blockRemover(String ip) {
        for (int i = ip.length()-1; i > 0; i--) {
            if (ip.charAt(i) == '.') {
                System.out.println(ip.substring(0, i+1));
                return ip.substring(0, i+1);
            }
        }
        return null;
    }

    private ArrayList<ICMPThread> createICMPThreads(MapCommand command) {
        ArrayList<ICMPThread> threads = new ArrayList<>(command.getEnd() - command.getStart());

        String address = blockRemover(command.getAddress().getHostAddress());

        try {
            for (int i = 0; i != command.getEnd() - command.getStart(); i++) {
                threads.add(new ICMPThread(InetAddress.getByName(address + (command.getStart() + i)), 5000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return threads;
    }

    //Creates tcp threads to break down the list of IPs into subsets of 10
    private TCPThread[] createTCPThreads(String ip, int quantity) {
        //Creates an array the size of the number of threads needed to hold the threads
        TCPThread[] tcpThreads = new TCPThread[quantity];
        ip = ip.substring(1);
        for (int i = 0; i < quantity; i++) {
            String address = ip + i;
            tcpThreads[i] = new TCPThread(address);
        }
        return tcpThreads;
    }

    public void pingCheck(MapCommand command) {

    }

    public void tcpCheck(String networkAddress, int start, int finish) throws IOException{
        TCPThread[] tcpThreads;

        //divides the total amount of IPs by 10
        double quantity = (double) (finish - start);
        //rounds up no matter tha value if it is not a whole number
        tcpThreads = createTCPThreads(networkAddress, (int) Math.ceil(quantity));

        ExecutorService threads = Executors.newFixedThreadPool((int) Math.ceil(quantity));

        for (TCPThread thread : tcpThreads) {
            threads.execute(thread);
        }

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            for (TCPThread thread : tcpThreads) {
                if (thread.liveHosts() > 0) {
                    liveHosts.putAll(thread.getHosts());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        console.displayPingResults(liveHosts);

        //Creating host objects from the results of the scan
        int index = 0;
        Host[] hosts = new Host[liveHosts.size()];
        Iterator it = liveHosts.entrySet().iterator();
        String os = "windows";
        String deviceName;
        while (it.hasNext()) {
            Map.Entry device = (Map.Entry)it.next();
            deviceName = (String) device.getValue();
            deviceName = deviceName.toLowerCase();
            if (deviceName.contains("android")) {
                os = "Android";
            }
            if (deviceName.contains("router")) {
                os = "Linux";
            }
            hosts[index] = new Host((String) device.getKey(), (String) device.getValue(), os);
            index ++;
        }
        updateDatabase(hosts);
    }

    public void updateDatabase(Host[] devices) {
        results.saveLiveHostResults(devices);
    }

}
