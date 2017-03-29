package Threads;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class PingThread extends Thread {

    private Thread t;
    private String ip;
    private int timeout;
    private HashMap<String, String> liveHosts;

    public PingThread(String ip, int timeout){
        this.ip = ip;
        this.timeout = timeout;
        liveHosts = new HashMap<>();
    }

    public HashMap<String, String> getHosts(){
        return liveHosts;
    }

    public int liveHosts() {
        return liveHosts.size();
    }

    public void run() {
        try {
            InetAddress ia = InetAddress.getByName(ip);
            if (ia.isReachable(timeout)) {
                liveHosts.put(ip, ia.getHostName());
                System.out.println(ia.getHostName());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void start(){
        if (t == null){
            t = new Thread(this, "PingThread");
            t.start();
        }
    }
}
