package Client;

import Threads.HeartBeatThread;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;


public class Ping {

    private InetAddress ip;
    private Console console;
    private Timer t;
    private Ping ping;
    private int sent, lost;
    private HeartBeatThread thread;

    Ping(Console console, String ip) {
        this.console = console;
        this.ping = this;
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        sent = 0;
        lost = 0;
    }

    public void sent() {
        sent++;
    }
    public void lost() {
        lost++;
    }

    public void stop() {
        t.cancel();
        thread.finish();
        int received = sent - lost;
        console.displayPingResult("Ping statistics for " + ip.toString().substring(1) + ":");
        console.displayPingResult("Packets sent = " + sent + " | packets received = " + received + " | packets lost = " + lost);
    }

    public void start() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                thread = new HeartBeatThread(ping, console, ip);
                thread.start();
            }
        }, 0, 2000);

    }
}
