package Client;

import Database.Port;
import Database.ResultsDatabase;
import Exceptions.ArgumentOverloadException;
import Threads.PortScanThread;
import Threads.UDPThread;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PortScanner {

    private HashMap<Integer, String> openPorts;
    private ResultsDatabase results;
    private Console console;

    public PortScanner(Console console) {
        openPorts = new HashMap<>();
        this.console = console;
        results = new ResultsDatabase();
    }

    private PortScanThread[] createPortScanThreads(InetAddress ip, double quantity, int fromPort, int toPort) {
        PortScanThread[] portThreads = new PortScanThread[(int) quantity];
        for (int i = 0; i < (int) quantity; i++) {
            //Makes the starting value of a thread 10 more than the thread before
            int beginning = (fromPort + (i * 5));
            //Adds 10 to the ending value of a thread giving it 10 IPs to ping
            int end = (((i + 1) * 5) + fromPort);
            //Checks if last thread is being created, if so change end for the finishing IP address
            if (end >= toPort) {
                portThreads[i] = new PortScanThread(ip, beginning, toPort);
                //System.out.println(name + " " + beginning + " " + toPort);
            } else {
                portThreads[i] = new PortScanThread(ip, beginning, end);
                //System.out.println(name + " " + beginning + " " + end);
            }
        }
        return portThreads;
    }

    private PortScanThread[] createPortScanThreads(InetAddress ip, ArrayList<Integer> toScan) {

        PortScanThread[] portThreads = new PortScanThread[toScan.size()];

        for (int i = 0; i < toScan.size(); i++) {
            portThreads[i] = new PortScanThread(ip, toScan.get(i));
        }

        return portThreads;
    }

    private UDPThread[] createUDPThreads(InetAddress ip, double quantity, int fromPort, int toPort) {

        UDPThread[] udpThreads = new UDPThread[(int) quantity];

        for (int i = 0; i < (int) quantity; i++) {
            //Makes the starting value of a thread 10 more than the thread before
            int beginning = (fromPort + (i * 5));
            //Adds 10 to the ending value of a thread giving it 10 IPs to ping
            int end = (((i + 1) * 5) + fromPort);
            //Increments the name of the thread
            String name = "PortScanThread" + (i + 1);
            //Checks if last thread is being created, if so change end for the finishing IP address
            if (end >= toPort) {
                udpThreads[i] = new UDPThread(name, ip, beginning, toPort);
            } else {
                udpThreads[i] = new UDPThread(name, ip, beginning, end);
            }
        }
        return udpThreads;
    }

    public void udpScan(InetAddress ip, int port) {
        UDPThread thread = new UDPThread("UDPThread1", ip, port);

        ExecutorService threads = Executors.newFixedThreadPool(1);

        threads.submit(thread);

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            if (thread.portCount() > 0) {
                openPorts.putAll(thread.getPorts());
            }

        } catch (Exception e) {
        }
    }

    public void udpScan(InetAddress ip, int fromPort, int toPort) {
        UDPThread[] udpThreads;

        double quantity = (double) (toPort - fromPort) / 5.0;

        udpThreads = createUDPThreads(ip, (int) Math.ceil(quantity), fromPort, toPort);

        ExecutorService threads = Executors.newFixedThreadPool((int) Math.ceil(quantity));

        for (UDPThread thread : udpThreads) {
            threads.submit(thread);
        }

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            for (UDPThread thread : udpThreads) {
                if (thread.portCount() > 0) {
                    openPorts.putAll(thread.getPorts());
                }
            }
        } catch (Exception e) {
        }
    }

    public void udpScan(InetAddress ip, int... ports) {

    }

    public void tcpScan(InetAddress ip) {
        PortScanThread[] portThreads = createPortScanThreads(ip, 1000, 1, 5000);

        ExecutorService threads = Executors.newFixedThreadPool(1000);

        for (PortScanThread thread : portThreads) {
            threads.submit(thread);
        }

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            for (PortScanThread thread : portThreads) {
                if (thread.portCount() > 0) {
                    openPorts.putAll(thread.getPorts());
                }
            }
        } catch (Exception e) {
        }

        int index = 0;
        Port[] ports = new Port[openPorts.size()];
        Iterator it = openPorts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry device = (Map.Entry)it.next();
            ports[index] = new Port(ip.getHostAddress(),(int) device.getKey(), (String) device.getValue());
            index ++;
        }
        saveResults(ports);
        console.displayPortResults(ports);
    }

    public void tcpScan(InetAddress ip, int port){
        PortScanThread thread = new PortScanThread(ip, port);

        ExecutorService threads = Executors.newFixedThreadPool(1);

        threads.submit(thread);

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            if (thread.portCount() > 0) {
                openPorts.putAll(thread.getPorts());
            }
        } catch (Exception e) {
        }
    }

    public void tcpScan(InetAddress ip, int fromPort, int toPort){
        PortScanThread[] portThreads;

        double quantity = (double) (toPort - fromPort) / 5.0;

        portThreads = createPortScanThreads(ip, (int) Math.ceil(quantity), fromPort, toPort);

        ExecutorService threads = Executors.newFixedThreadPool((int) Math.ceil(quantity));

        for (PortScanThread thread : portThreads) {
            threads.submit(thread);
        }

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            for (PortScanThread thread : portThreads) {
                if (thread.portCount() > 0) {
                    openPorts.putAll(thread.getPorts());
                }
            }
        } catch (Exception e) {
        }

        int index = 0;
        Port[] ports = new Port[openPorts.size()];
        Iterator it = openPorts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry device = (Map.Entry)it.next();
            ports[index] = new Port(ip.getHostAddress(),(int) device.getKey(), (String) device.getValue());
            index ++;
        }
        saveResults(ports);
        console.displayPortResults(ports);
    }

    public void tcpScan(InetAddress ip, int... ports) throws ArgumentOverloadException {
        if (ports.length > 10) {
            throw new ArgumentOverloadException("Too many arguments used...");
        }
        ArrayList<Integer> toScan = new ArrayList<>();
        PortScanThread[] portThreads;

        for (int port : ports) {
            toScan.add(port);
        }

        double quantity;

        if (toScan.size() >= 5) {
            quantity = (double) (toScan.size()) / 5.0;
        } else {
            quantity = toScan.size();
        }

        portThreads = createPortScanThreads(ip, toScan);

        ExecutorService threads = Executors.newFixedThreadPool((int) Math.ceil(quantity));

        for (PortScanThread thread : portThreads) {
            threads.submit(thread);
        }

        threads.shutdown();

        try {
            //Forces application to wait for all threads to finish before proceeding
            threads.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            for (PortScanThread thread : portThreads) {
                if (thread.portCount() > 0) {
                    openPorts.putAll(thread.getPorts());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void saveResults(Port[] ports) {
        results.savePortScanResults(ports);
    }

}
