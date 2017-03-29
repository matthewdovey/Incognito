package Threads;

import Client.Console;
import Client.Ping;

import java.io.IOException;
import java.net.InetAddress;

public class HeartBeatThread extends Thread {

    private Thread t;
    private InetAddress ip;
    private Console console;
    private Ping ping;
    private boolean running;

    public HeartBeatThread(Ping ping, Console console, InetAddress ip) {
        this.ip = ip;
        this.console = console;
        this.ping = ping;
        running = false;
    }

    public void finish() {
        running = false;
    }

    public void run() {
        try {
            final long startTime = System.currentTimeMillis();
            if (running) {
                if (ip.isReachable(2000)) {
                    final long endTime = System.currentTimeMillis();
                    console.displayPingResult("Reply from " + ip.toString().substring(1) + ": time=" + (endTime - startTime) + "ms");
                    ping.sent();
                } else {
                    console.displayPingResult("Reply from " + ip.toString().substring(1) + ": destination host unreachable");
                    ping.lost();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void start() {
        if (t == null){
            t = new Thread(this, "PingThread");
            t.start();
        }
        running = true;
    }
}
