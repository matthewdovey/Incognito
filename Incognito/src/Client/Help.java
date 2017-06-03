package Client;

import java.util.ArrayList;

public class Help {

    private Console console;

    public Help(Console console) {
        this.console = console;
    }

    public void helpMenu(ArrayList<String> displayHistory) {
        String helpMenu = "help - display all valid commands\nscan - \nclear - clears console of past commands\n" +
                "ping - \nreport - requests the creation of the exportable report\nexit - exits out of Incognito";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += helpMenu;
        displayHistory.add(helpMenu);
        console.printToConsole(output);
    }

    public void help(ArrayList<String> displayHistory, String filter) {
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
        console.printToConsole(output);
    }
}
