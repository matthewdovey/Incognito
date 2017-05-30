package Commands;

import java.net.InetAddress;
import java.util.ArrayList;

public final class ScanCommand extends Command{

    private final InetAddress address;
    private final int fromPort, toPort;
    private final ArrayList<Integer> ports;

    public ScanCommand(InetAddress address) {
        this.address = address;
        this.fromPort = 1;
        this.toPort = 5000;
        this.ports = null;
    }

    public ScanCommand(InetAddress address, int port) {
        this.address = address;
        this.fromPort = port;
        this.toPort = port;
        this.ports = null;
    }

    public ScanCommand(InetAddress address, int fromPort, int toPort) {
        this.address = address;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.ports = null;
    }

    public ScanCommand(InetAddress address, int ...ports) {
        this.ports = new ArrayList<>();
        this.address = address;
        this.fromPort = 0;
        this.toPort = 0;
        for (Integer port : ports) {
            this.ports.add(port);
        }
    }

    public final InetAddress getAddress() {
        return address;
    }

    @Override
    public final int getStart() {
        return fromPort;
    }

    @Override
    public final int getEnd() {
        return toPort;
    }

    public final ArrayList<Integer> getPorts() {
        if (this.ports.size() > 0) {
            return this.ports;
        }
        return null;
    }
}
