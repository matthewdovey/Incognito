package Commands;

import java.net.InetAddress;

public final class MapCommand extends Command{

    private final InetAddress address;
    private final int start, end;
    private final boolean advancedCheck;

    public MapCommand(InetAddress address) {
        this.address = address;
        this.start = 1;
        this.end = 255;
        this.advancedCheck = false;
    }

    public MapCommand(InetAddress address, int start) {
        this.address = address;
        this.start = start;
        this.end = 255;
        this.advancedCheck = false;
    }

    public MapCommand(InetAddress address, int start, int end) {
        this.address = address;
        this.start = start;
        this.end = end;
        this.advancedCheck = false;
    }

    public MapCommand(InetAddress address, int start, int end, Boolean advancedCheck) {
        this.address = address;
        this.start = start;
        this.end = end;
        this.advancedCheck = advancedCheck;
    }

    public final InetAddress getAddress() {
        return this.address;
    }

    @Override
    public final int getStart() {
        return this.start;
    }

    @Override
    public final int getEnd() {
        return this.end;
    }

    public final boolean isAdvancedCheck() {
        return this.advancedCheck;
    }
}
