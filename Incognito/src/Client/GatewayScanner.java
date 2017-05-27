package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GatewayScanner {

    private ArrayList<String> gateways;

    public ArrayList<String> getGateways() {
        gateways = findGateway();
        return gateways;
    }

    public String getGateway() {
        gateways = findGateway();
        if (gateways.size() > 1) {
            return findGateway().get(1);
        }
        return findGateway().get(0);
    }

    private ArrayList<String> findGateway() {
        ProcessBuilder builder = new ProcessBuilder("ipconfig");
        builder.redirectErrorStream(true);
        ArrayList<String> ips = new ArrayList<>();
        String ip;
        try {
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                if (line.contains("IPv4")) {
                    ip = line.substring(line.length() - 15);
                    ip = ip.replaceAll("[^\\d.]", "");
                    int index = ip.lastIndexOf('.');
                    ip = ip.substring(0, index+1);
                    ip += "1";
                    ips.add(ip);
                }
            }
        } catch (Exception e) {
        }
        return ips;
    }
}
