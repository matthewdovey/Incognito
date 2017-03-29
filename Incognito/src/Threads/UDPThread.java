package Threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * Created by Matthew on 07/02/2017.
 */
public class UDPThread extends Thread {

    private Thread t;
    private int fromPort, toPort, port;
    private String thread;
    private HashMap<Integer, String> openPorts;
    private InetAddress ip;

    public UDPThread(String thread, InetAddress ip, int port) {
        this.thread = thread;
        this.port = port;
        this.ip = ip;
        openPorts = new HashMap<>();
    }

    public UDPThread(String thread, InetAddress ip, int fromPort, int toPort) {
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.thread = thread;
        this.ip = ip;
        openPorts = new HashMap<>();
    }

    public HashMap<Integer, String> getPorts(){
        return openPorts;
    }

    public int portCount() {
        return openPorts.size();
    }

    public String name(){
        return thread;
    }

    public void run() {
        for (int i = fromPort; i < toPort; i++) {
            String message = "Don't mind me...";
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length());
            DatagramPacket receivePacket = new DatagramPacket(message.getBytes(), message.length());
            try {

                DatagramSocket socket = new DatagramSocket();
                socket.setSoTimeout(3000);
                socket.connect(ip, i);
                socket.send(packet);
                //System.out.println(socket.isConnected());
                socket.receive(receivePacket);
                socket.close();
                System.out.println(i + ": Open");
                openPorts.put(i, "Open");
            } catch (SocketTimeoutException timeout) {
                System.out.println(i + ": Open | Filtered");
            } catch (IOException e) {
                System.out.println(i + ": Closed");
            }
        }
    }

    public void start() {
        if (t == null){
            t = new Thread(this, thread);
            t.start();
        }
    }
}
