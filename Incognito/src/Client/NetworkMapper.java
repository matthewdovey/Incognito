package Client;

import Application.Controller;
import Database.Host;
import Database.ResultsDatabase;
import Threads.PingThread;
import Threads.TCPThread;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

    //Creates ping threads to break down the list of IPs into subsets of 10
    private PingThread[] createPingThreads(InetAddress ip, double quantity, int start) {
        //Creates an array the size of the number of threads needed to hold the threads
        PingThread[] pingThreads = new PingThread[(int) quantity];
        String address = ip.toString().substring(1, ip.toString().length()-1);
        for (int i = 0; i < (int) quantity; i++) {
            int num = start + i;
            String ia = address + num;
            pingThreads[i] = new PingThread(ia, timeout);
        }
        System.out.println(pingThreads.length + " PingThreads were created...");
        return pingThreads;
    }

    //Creates tcp threads to break down the list of IPs into subsets of 10
    private TCPThread[] createTCPThreads(String ip, int quantity, int start, int finish) {
        //Creates an array the size of the number of threads needed to hold the threads
        TCPThread[] tcpThreads = new TCPThread[quantity];

        ip = ip.substring(1);
        System.out.println(ip + "-------------------------------------------------------------------------");

        for (int i = 0; i < quantity; i++) {
            String address = ip + i;
            tcpThreads[i] = new TCPThread(address);
        }
        System.out.println(tcpThreads.length + " TCPThreads were created...");
        return tcpThreads;
    }

    //Starts and collects all data from ping threads, returns boolean based on if any live hosts found
    public String pingCheck(String networkAddress, int start, int finish) throws IOException {

        console.displayPingResult("Pinging addresses...");
        PingThread[] pingThreads;

        //divides the total amount of IPs by 10
        double quantity = (double) (finish - start);
        //rounds up no matter tha value if it is not a whole number
        pingThreads = createPingThreads(InetAddress.getByName(networkAddress),(int) Math.ceil(quantity), start);

        final ExecutorService threads = Executors.newFixedThreadPool((int) Math.ceil(quantity));

        for (PingThread thread : pingThreads) {
            threads.execute(thread);
        }

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println("All pingThreads finished...");

            for (PingThread thread : pingThreads) {
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
        while (it.hasNext()) {
            Map.Entry device = (Map.Entry)it.next();
            hosts[index] = new Host((String) device.getKey(), (String) device.getValue(),"Windows");
            index ++;
        }
        updateDatabase(hosts);
        return "Ping check finished...";
    }

    public void tcpCheck(String networkAddress, int start, int finish) throws IOException{
        TCPThread[] tcpThreads;

        //divides the total amount of IPs by 10
        double quantity = (double) (finish - start);
        //rounds up no matter tha value if it is not a whole number
        tcpThreads = createTCPThreads(networkAddress, (int) Math.ceil(quantity), start, finish);

        ExecutorService threads = Executors.newFixedThreadPool((int) Math.ceil(quantity));

        for (TCPThread thread : tcpThreads) {
            threads.execute(thread);
        }

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println("All tcpThreads finished...");

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
        System.out.println("Tcp check finished...");
    }

    public void updateDatabase(Host[] devices) {
        results.saveLiveHostResults(devices);
        results.displayTable("hosts");
    }

    public HashMap<String, String> liveHosts() {
        return liveHosts;
    }

}
