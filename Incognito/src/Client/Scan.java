package Client;

import java.util.ArrayList;
import java.util.List;

public class Scan {

    private ArrayList<String> scans;

    public Scan() {
        getScans();
    }

    public boolean scan(List<String> arguments) {
        System.out.println("we get in scan");
        for (String argument : arguments) {
            System.out.println(argument);
            if (!isScan(argument)) {
                System.out.println("invalid scan..");
                return false;
            }
        }

        System.out.println("valid scan..");
        createScan(arguments);

        return true;
    }

    public boolean isScan(String command) {
        System.out.println("checking if valid..");
        return scans.contains(command); }

    private void getScans() {
        //TODO: work out all of the possible scans that wil be available
        scans = new ArrayList<>();
        scans.add("-o");
        scans.add("-fw");
        scans.add("-av");
        scans.add("-p");
        scans.add("-pAll");
        scans.add("-all");
    }

    private void createScan(List<String> arguments) {
        //TODO: Set up scan with options in arguments, so are they looking for the OS? Anti virus? ETC...
        boolean os, av, fw, all = false;
        for (String argument : arguments) {
            System.out.println(argument);
            switch (argument) {
                case "-o":
                    os = true;
                    break;
                case "-av":
                    av = true;
                    break;
                case "-fw":
                    fw = true;
                    break;
                case "-all":
                    all = true;
                    break;
            }
        }
    }
}
