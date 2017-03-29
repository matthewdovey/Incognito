package Threads;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

//TODO: Maybe scan each port three times before making a decision on whether or not it is filtered, if open then break loop early, if closed break loop early, if filtered try 3 times in total.

public class PortScanThread extends Thread {

    private Thread t;
    private InetAddress ip;
    private int start, finish, port;
    private HashMap<Integer, String> openPorts;
    private int version;

    public PortScanThread(InetAddress ip) {
        openPorts = new HashMap<>();
        this.ip = ip;
        start = 1;
        finish = 1000;
        version = 1;
    }

    public PortScanThread(InetAddress ip, int port) {
        openPorts = new HashMap<>();
        this.ip = ip;
        this.port = port;
        version = 2;
    }

    public PortScanThread(InetAddress ip, int start, int finish) {
        openPorts = new HashMap<>();
        this.ip = ip;
        this.start = start;
        this.finish = finish;
        version = 1;
    }

    public HashMap<Integer, String> getPorts(){
        return openPorts;
    }

    public int portCount() {
        return openPorts.size();
    }

    public void run(){
        Socket s = null;

        switch (version) {
            case 1:
                for (int i = start; i < finish; i++) {
                    try
                    {
                        s = new Socket();
                        s.connect(new InetSocketAddress(ip, i), 1000);
                        //System.out.println(ip + " - port " + i + ": open");
                        openPorts.put(i, "Open");
                    }
                    catch (Exception e)
                    {
                    }
                    finally
                    {
                        if(s != null) {
                            try {
                                s.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                break;
            case 2:
                try
                {
                    s = new Socket(ip, port);
                    //System.out.println(ip + " " + port + ": open");
                    openPorts.put(port, "Open");
                }
                catch (Exception e) {
                    System.out.println("closed");
                }
                finally
                {
                    if(s != null)
                        try {
                            s.close();
                        }
                        catch(Exception e){

                        }
                }
                break;
        }
    }

    public void start(){
        if (t == null){
            t = new Thread(this, "PortScanThread");
            t.start();
        }
    }
}
