package Commands;

import java.net.InetAddress;

/**
 * Created by Matthew on 29/05/2017.
 */
public class Command {

    public InetAddress address;
    public int start, end;

    public InetAddress getAddress() {
        return address;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
