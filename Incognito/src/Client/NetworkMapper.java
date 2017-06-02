package Client;

import Commands.Command;
import Commands.MapCommand;
import Database.Host;
import Database.ResultsDatabase;
import Threads.ICMPThread;
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

    NetworkMapper(Console console) {
        liveHosts = new HashMap<>();
        this.console = console;
        results = new ResultsDatabase();
    }

    public void map(Command command) {
        ArrayList<ICMPThread> threads = createICMPThreads((MapCommand) command);

        final ExecutorService executor = Executors.newFixedThreadPool(threads.size());

        threads.forEach(x -> executor.submit(x));

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        threads.stream().filter(s -> s.liveHosts() > 0).forEach(s -> liveHosts.putAll(s.getHosts()));

        console.displayLiveHosts(liveHosts);

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

    public void tcpScan(MapCommand command) throws IOException{
        ArrayList<TCPThread> threads = createTCPThreads(command);

        ExecutorService executor = Executors.newFixedThreadPool(command.getEnd() - command.getStart());

        threads.forEach(x -> executor.submit(x));

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        threads.stream().filter(s -> s.liveHosts() > 0).forEach(s -> liveHosts.putAll(s.getHosts()));

        console.displayLiveHosts(liveHosts);

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

    private ArrayList<TCPThread> createTCPThreads(MapCommand command) {
        ArrayList<TCPThread> threads = new ArrayList<>(command.getEnd() - command.getStart());

        String address = blockRemover(command.getAddress().getHostAddress());

        for (int i = 0; i != command.getEnd() - command.getStart(); i++) {
            threads.add(new TCPThread(address + (command.getStart() + i)));
        }

        return threads;
    }

    public void updateDatabase(Host[] devices) {
        results.saveLiveHostResults(devices);
    }

}
