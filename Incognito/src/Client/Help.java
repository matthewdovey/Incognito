package Client;

import java.util.ArrayList;

/**
 * Created by Matthew on 11/12/2016.
 */
public class Help {

    public Help() {

    }

    public void helpMenu(ArrayList<String> displayHistory) {
        System.out.println("help");
        String helpMenu = "help - display all valid commands\nscan - \nclear - clears console of past commands\n" +
                "ping - \nreport - requests the creation of the exportable report\nexit - exits out of Incognito";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += helpMenu;
        displayHistory.add(helpMenu);
        Console.consoleOutput.setText(output);
        Console.consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    public void help(ArrayList<String> displayHistory, String filter) {
        System.out.println("help");
        String specificHelp = "";
        switch (filter) {
            case "map":
                specificHelp = "\nmap - discovers all live hosts on a network \n" +
                        "\nUsage: \n" +
                        "map target_network from_IP to_IP [-t timeout] \n" +
                        "map target_network from_IP to_IP \n" +
                        "map target_network \n" +
                        "\nOptions: \n" +
                        "-t       timeout in milliseconds to receive reply \n" +
                        "-a       advanced scan using TCP connections";
                break;
            case "scanner":
                specificHelp = "\nscanner - scans for open ports on a target host\n" +
                        "\nUsage: \n" +
                        "scanner target_IP from_port to_port [-a] \n" +
                        "\nOptions: \n" +
                        "-a       advanced port scan (additional UDP scans)";
                break;
            case "scan":
                specificHelp = "\nscan - scans for open ports on a target host\n" +
                        "\nUsage: \n" +
                        " \n" +
                        "\nOptions: \n" +
                        "";
                break;
            case "clear":
                specificHelp = "clear - clears console of past commands";
                break;
            case "exit":
                specificHelp = "exit - exits out of Incognito";
                break;
            case "report":
                specificHelp = "report - requests the creation of the exportable report";
                break;
            case "ping":
                specificHelp = "ping - ";
                break;
            default:
                helpMenu(displayHistory);
        }
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += specificHelp;
        displayHistory.add(specificHelp);
        Console.consoleOutput.setText(output);
        Console.consoleOutput.setScrollTop(Double.MAX_VALUE);
    }
}
