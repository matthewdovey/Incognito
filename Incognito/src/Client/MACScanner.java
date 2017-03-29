package Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class MACScanner {

    private GatewayScanner gatewayScanner;

    public MACScanner() {
        gatewayScanner = new GatewayScanner();
    }

    public String scan() {
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        /***************************************************************************
         * First get a list of devices on this system
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf
                    .toString());
            return "Unknown";
        }

        /***************************************************************************
         * Second iterate through all the interface and get the HW addresses
         **************************************************************************/
        try {
            for (final PcapIf i : alldevs) {
                final byte[] mac = i.getHardwareAddress();
                if (mac == null) {
                    continue; // Interface doesn't have a hardware address
                }
                System.out.printf("%s=%s\n", i.getName(), asString(mac));
                System.out.println(i.getName());
                System.out.println(i.getAddresses());
                System.out.println("");
                if (i.getAddresses().contains(gatewayScanner.getGateway())) {
                    System.out.println("found");
                    System.out.println(i.getAddresses());
                    System.out.println("");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Found";
    }

    public static void main(String[] args) throws IOException {

        MACScanner scanner = new MACScanner();
        System.out.println(scanner.scan());
    }

    private static String asString(final byte[] mac) {
        final StringBuilder buf = new StringBuilder();
        for (byte b : mac) {
            if (buf.length() != 0) {
                buf.append(':');
            }
            if (b >= 0 && b < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString((b < 0) ? b + 256 : b).toUpperCase());
        }

        return buf.toString();
    }
}
