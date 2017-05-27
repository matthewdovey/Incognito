package Client;
import Application.Config;
import Commands.MapCommand;
import Database.Port;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class Console extends BorderPane{
    private TextField consoleInput;
    public static TextArea consoleOutput;
    private ArrayList<String> consoleHistory;
    private ArrayList<String> displayHistory;
    private int index = 0;
    private PortScanner scan;
    private Help helper;
    private Report report;
    private Command command;
    private Ping ping;
    private NetworkMapper mapper;

    //TODO: How do I want the output of these commands to be shown....
    //TODO: I guess in the console output window...
    //TODO: How much do I want to allow the user to do?
    //TODO: I think get the basics done, then if there is time make it more advanced...

    public Console() {
        scan = new PortScanner(this);
        helper = new Help();
        report = new Report();
        command = new Command();
        mapper = new NetworkMapper(this);

        Config.setConsole(this);

        consoleInput = new TextField();
        consoleOutput = new TextArea();
        consoleHistory = new ArrayList<>();
        displayHistory = new ArrayList<>();

        consoleInput.setStyle("-fx-background-color: #000000;" + "-fx-text-inner-color: white;");
        consoleOutput.setEditable(false);
        setCenter(consoleOutput);
        setBottom(consoleInput);

        displayPingResult("Incognito [version 1.0]");

        consoleInput.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case ENTER:
                    consoleInputHandler(consoleInput.getText());
                    index = 0;
                    consoleInput.clear();
                    break;
                case UP:
                    if (!consoleHistory.isEmpty()) {
                        if (index == consoleHistory.size()) {
                            index = 0;
                        }
                        consoleInput.setText(consoleHistory.get(index));
                        index++;
                    }
                    break;
                case DOWN:
                    if (!consoleHistory.isEmpty() && index > 0) {
                        consoleInput.setText((consoleHistory.get(index-1)));
                        index--;
                    }
                    break;
                case C:
                    if (event.isControlDown()) {
                        System.out.println("Ctrl-C");
                        ping.stop();
                    }
            }
        });
    }

    private void consoleInputHandler(String input) {
        input = input.toLowerCase();
        String[] commandWords = input.split("\\s+");
        displayHistory.add(input);
        if (command.isCommand(commandWords[0])) {
            consoleHistory.add(0,input);
            updateOutput();
            switch (commandWords[0]) {
                case "help":
                    if (commandCheck(commandWords)) {
                        help(commandWords);
                    } else {

                    }
                    break;
                case "scan":
                    if (commandCheck(commandWords)) {
                        scan(commandWords);
                    } else {

                    }
                    break;
                case "clear":
                    if (commandCheck((commandWords))) {
                        clear();
                    } else {

                    }
                    break;
                case "ping":
                    if (commandCheck(commandWords)) {
                        ping(commandWords);
                    } else {

                    }
                    break;
                case "report":
                    if (commandCheck(commandWords)) {

                    } else {
                        report(commandWords);
                    }
                    break;
                case "map":
                    if (commandCheck(commandWords)) {
                        mapper(commandWords);
                    } else {
                        helper.help(displayHistory, "map");
                    }
                    break;
                case "exit":
                    if (commandCheck(commandWords)) {
                        exit(commandWords);
                    } else {

                    }
                    break;
            }
        } else {
            displayErrorMessage();
        }
    }

    public boolean commandCheck(String[] commandWords) {
        switch (commandWords[0]) {
            case "help":
                if (!lengthCheck(commandWords)) {
                    return false;
                }
                break;
            case "scan":
                if (!lengthCheck(commandWords)) {
                    return false;
                }
                break;
            case "clear":
                if (!lengthCheck(commandWords)) {
                    return false;
                }
                break;
            case "ping":
                if (!lengthCheck(commandWords)) {
                    return false;
                }
                break;
            case "report":
                if (!lengthCheck(commandWords)) {
                    return false;
                }
                break;
            case "map":
                if (!isIP(commandWords[1])) {
                    displayPingResult("Invalid IP address...");
                    return false;
                }
                if (!lengthCheck(commandWords)) {
                    displayPingResult("Invalid number of arguments...");
                    return false;
                }
                break;
            case "exit":
                if (!lengthCheck(commandWords)) {
                    return false;
                }
                break;
        }
        return true;
    }

    private boolean lengthCheck(String[] commandWords) {
        switch (commandWords[0]) {
            case "help":
                if (commandWords.length > 2) {
                    return false;
                }
                break;
            case "scan":

                break;
            case "clear":
                if (commandWords.length > 1) {
                    return false;
                }
                break;
            case "ping":
                if (commandWords.length > 2) {
                    return false;
                }
                break;
            case "report":
                if (commandWords.length > 2) {
                    return false;
                }
                break;
            case "map":
                if (commandWords.length < 2 || commandWords.length > 5) {
                    return false;
                }
                break;
            case "exit":
                exit(commandWords);
                break;
        }
        return true;
    }

    private boolean rangeCheck(String number) {
        if (Integer.parseInt(number) > 0 && Integer.parseInt(number) <= 255) {
            return true;
        }
        return false;
    }

    private void help(String[] commandWords) {
        if (commandWords.length > 2) {
            displayErrorMessage();
        } else if (commandWords.length == 2){
            helper.help(displayHistory, commandWords[1]);
        } else {
            helper.helpMenu(displayHistory);
        }
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void clear() {
        consoleOutput.clear();
        displayHistory.clear();
    }

    private void ping(String[] commandWords) {
        if (commandWords.length == 1 || commandWords.length > 2) {
            helper.help(displayHistory, "ping");
        } else if (!isIP(commandWords[1])){
            displayIpErrorMessage();
        } else {
            ping = new Ping(this, commandWords[1]);
            ping.start();
        }
    }

    private InetAddress stringToIP(String ip){
        try {
            return InetAddress.getByName(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mapper(String[] commandWords) {
        ExecutorService service = Executors.newFixedThreadPool(1);

        MapCommand command;

        if (commandWords.length == 2) {
            command = new MapCommand(stringToIP(commandWords[1]));

            mapper.map(command);

        } else if (commandWords.length == 3) {
            command = new MapCommand(stringToIP(commandWords[1]), Integer.parseInt(commandWords[2]));

            mapper.map(command);
        } else if (commandWords.length == 4) {
            command = new MapCommand(stringToIP(commandWords[1]), Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]));

            mapper.map(command);


        } else if (commandWords.length == 5) {
            command = new MapCommand(stringToIP(commandWords[1]), Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]), true);

            mapper.map(command);
        }
    }

    private void scan(String[] commandWords) {
        if (commandWords.length == 4) {
            try {
                scan.tcpScan(InetAddress.getByName(commandWords[1]), Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]));
            } catch (UnknownHostException e) {
                System.out.println(e);
            }
        } else if (commandWords.length == 5) {
            if (isIP(commandWords[1])) {
                if (rangeCheck(commandWords[2]) && rangeCheck(commandWords[3])) {
                    if (commandWords[4].equals("-a")) {
                        NetworkMapper map = new NetworkMapper(this, Integer.parseInt(commandWords[5]));
//                        try {
//                            map.pingCheck(commandWords[1], Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]));
//                        } catch (IOException e) {
//                            System.out.println(e);
//                        }
                    } else {
                        //Output invalid command issued...
                    }
                }
            }
        } else if (commandWords.length == 2) {
            try {
                scan.tcpScan(InetAddress.getByName(commandWords[1]));
            } catch (Exception e) {

            }
        } else {
            helper.help(displayHistory, "scanner");
        }
    }

    private void report(String[] commandWords) {
        if (commandWords.length == 1) {
            helper.help(displayHistory, "report");
        } else {
            List<String> followingCommands = Arrays.asList(commandWords).subList(1, commandWords.length);
        }
    }

    private void exit(String[] commandWords) {
        if (commandWords.length == 1) {
            Platform.exit();
        } else {
            displayErrorMessage();
        }
    }

    public void displayPingResults(HashMap<String, String> results) {
        results.forEach((k, v) -> displayHistory.add(v + " " + k));
        updateOutput();
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    public void displayPingResult(String result) {
        displayHistory.add(result);
        updateOutput();
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

//    public void displayPortResults(HashMap<Integer, String> results) {
//        results.forEach((k, v) -> displayHistory.add("Port " + k + ": " + v));
//        updateOutput();
//        consoleOutput.setScrollTop(Double.MAX_VALUE);
//    }

    public void displayPortResults(Port[] ports) {
        System.out.println("Trying to display port results...");
        int i = 0;
        for (Port port : ports) {
            System.out.println("Port " + i++ + ": " + port.getPort());
            displayHistory.add("Port " + i++ + ": " + port.getPort());
        }
        updateOutput();
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void updateOutput() {
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void displayErrorMessage() {
        String invalid = "Invalid command entered...";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += invalid;
        displayHistory.add(invalid);
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void displayIpErrorMessage() {
        String invalid = "Invalid ip entered...";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += invalid;
        displayHistory.add(invalid);
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private boolean isIP(String ip) {
        //TODO: take arguments after scan and check if it is an IP address if the argument does not match any scan options
        int count = 0;
        if (ip.isEmpty()) {
            return false;
        }
        for (int i = 0; i < ip.length(); i++) {
            if (!Character.isDigit(ip.charAt(i))) {
                if (ip.charAt(i) != '.') {
                    if (ip.charAt(i) != '-') {
                        return false;
                    }
                }
            }
        }
        String[] segments = ip.split("\\.");
        if (segments.length != 4) {
            return false;
        }
        if (ip.contains("-")) {
            for (String segment : segments) {
                if (segment.contains("-")) {
                    //TODO: might need to implement this into the for loop below as
                    //TODO: the segment length check will return false as the length > 3
                }
                count++;
            }
            //TODO: I think I need to iterate through all of the segments, check which has the "-"
            //TODO: remember which has and then act accordingly
        }
        for (String segment : segments) {
            if (segment.length() < 1 || segment.length() > 3 || Integer.parseInt(segment) > 255 || Integer.parseInt(segment) < 0) {
                displayErrorMessage();
                return false;
            }
        }
        return true;
    }
}
