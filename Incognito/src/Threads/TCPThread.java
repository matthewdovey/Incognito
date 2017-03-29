package Threads;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPThread extends Thread {

    private Thread t;
    private String ip;
    private HashMap<String, String> liveHosts;
    private ArrayList<Integer> ports;

    public TCPThread(String ip) {
        liveHosts = new HashMap<>();
        this.ip = ip;

        ports = new ArrayList<>();
        ports.add(21);
        ports.add(22);
        ports.add(23);
        ports.add(25);
        ports.add(53);
        ports.add(80);
        ports.add(100);
    }

    public HashMap<String, String> getHosts(){
        return liveHosts;
    }

    public int liveHosts() {
        return liveHosts.size();
    }


    public void run() {
        PortScanThread[] threads = new PortScanThread[ports.size()];

        ExecutorService subThreads = Executors.newFixedThreadPool(ports.size());

        for (int i = 0; i < ports.size(); i++) {
            try {
                threads[i] = new PortScanThread(InetAddress.getByName(ip), ports.get(i));
                subThreads.submit(threads[i]);
            } catch (IOException e) {
            }
        }
        subThreads.shutdown();

        Boolean live = false;

        for (PortScanThread thread : threads) {
            if (thread.portCount() > 0) {
                live = true;
            }
        }

        if (live) {
            try {
                liveHosts.put(ip, InetAddress.getByName(ip).getHostName());
            } catch (UnknownHostException e) {
                System.out.println(e);
            }

        }
    }

    public void start(){
        if (t == null){
            t = new Thread(this, "TCPThread");
            t.start();
        }
    }
}
