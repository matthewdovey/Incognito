package Threads;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

/**
 * Created by Matthew on 27/05/2017.
 */
public class ICMPThread implements Runnable {

    private InetAddress ip;
    private int timeout;
    private HashMap<String, String> liveHosts;

    public ICMPThread(InetAddress ip, int timeout){
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
        System.out.println(this.ip + ": Thread running...");
        try {
            if (ip.isReachable(timeout)) {
                System.out.println(this.ip + ": online");
                liveHosts.put(ip.getHostAddress(), ip.getHostName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
