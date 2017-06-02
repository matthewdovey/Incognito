package Client;
import Application.Config;
import Commands.Command;
import Commands.MapCommand;
import Commands.ScanCommand;
import Database.Port;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Console extends BorderPane{
    private TextField consoleInput;
    public static TextArea consoleOutput;
    private ArrayList<String> consoleHistory;
    private ArrayList<String> displayHistory;
    private int index = 0;
    private PortScanner scanner;
    private Help helper;
    private Report report;
    private AvailableCommands availableCommands;
    private Ping ping;
    private NetworkMapper mapper;
    private Command command;
    private IpAddressValidator validator;

    public Console() {
        scanner = new PortScanner(this);
        helper = new Help();
        report = new Report();
        availableCommands = new AvailableCommands();
        mapper = new NetworkMapper(this);
        validator = new IpAddressValidator();

        Config.setConsole(this);

        consoleInput = new TextField();
        consoleOutput = new TextArea();
        consoleHistory = new ArrayList<>();
        displayHistory = new ArrayList<>();

        consoleInput.setStyle("-fx-background-color: #000000;" + "-fx-text-inner-color: white;");
        consoleOutput.setEditable(false);
        setCenter(consoleOutput);
        setBottom(consoleInput);

        printToConsole("Incognito [version 1.0]");

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
        String[] commandWords = input.toLowerCase().split("\\s+");
        displayHistory.add(input);
        consoleHistory.add(0,input);
        updateOutput();
        if (availableCommands.isCommand(commandWords[0])) {
            switch (commandWords[0]) {
                case "help":
                    if (lengthCheck(commandWords)) {
                        help(commandWords);
                    } else {
                        invalidNumberOfArguments();
                        helper.help(displayHistory, "help");
                    }
                    break;
                case "scan":
                    if (lengthCheck(commandWords)) {
                        if (validator.isValid(commandWords[1])) {
                            scan(commandWords);
                        } else {
                            invalidIPAddress();
                            helper.help(displayHistory, "scan");
                        }
                    } else {
                        invalidNumberOfArguments();
                        helper.help(displayHistory, "scan");
                    }
                    break;
                case "clear":
                    if (lengthCheck((commandWords))) {
                        clear();
                    } else {
                        invalidNumberOfArguments();
                        helper.help(displayHistory, "clear");
                    }
                    break;
                case "ping":
                    if (lengthCheck(commandWords)) {
                        if (validator.isValid(commandWords[1])) {
                            ping(commandWords);
                        } else {
                            invalidIPAddress();
                            helper.help(displayHistory, "ping");
                        }
                    } else {
                        invalidNumberOfArguments();
                        helper.help(displayHistory, "ping");
                    }
                    break;
                case "report":
                    if (lengthCheck(commandWords)) {
                        report(commandWords);
                    } else {
                        invalidNumberOfArguments();
                        helper.help(displayHistory, "report");
                    }
                    break;
                case "map":
                    if (lengthCheck(commandWords)) {
                        if (validator.isValid(commandWords[1])) {
                            mapper(commandWords);
                        } else {
                            invalidIPAddress();
                            helper.help(displayHistory, "map");
                        }
                    } else {
                        invalidNumberOfArguments();
                        helper.help(displayHistory, "map");
                    }
                    break;
                case "exit":
                    if (lengthCheck(commandWords)) {
                        exit();
                    } else {
                        invalidNumberOfArguments();
                        helper.help(displayHistory, "exit");
                    }
                    break;
            }
        } else {
            invalidCommand();
        }
    }

    private boolean lengthCheck(String[] commandWords) {
        switch (commandWords[0]) {
            case "help":
                if (commandWords.length != 2) {
                    return false;
                }
                break;
            case "scan":
                if (commandWords.length < 2) {
                    return false;
                }
                break;
            case "clear":
                if (commandWords.length != 1) {
                    return false;
                }
                break;
            case "ping":
                if (commandWords.length != 2) {
                    return false;
                }
                break;
            case "report":
                if (commandWords.length != 2) {
                    return false;
                }
                break;
            case "map":
                if (commandWords.length < 2 || commandWords.length > 5) {
                    return false;
                }
                break;
            case "exit":
                if (commandWords.length != 1) {
                    return false;
                }
                break;
        }
        return true;
    }

    private boolean portCheck(String port) {
        if (Integer.parseInt(port) > 0 && Integer.parseInt(port) <= 65535) {
            return true;
        }
        return false;
    }

    private void help(String[] commandWords) {
        helper.help(displayHistory, commandWords[1]);
    }

    private void clear() {
        consoleOutput.clear();
        displayHistory.clear();
    }

    private void ping(String[] commandWords) {
        ping = new Ping(this, commandWords[1]);
        ping.start();
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
        if (commandWords.length == 2) {
            System.out.println("Mapper");
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
        if (Arrays.asList(commandWords).contains("-udp")) {
            if (commandWords.length == 3) {
                command = new ScanCommand(stringToIP(commandWords[1]));
                scanner.udpScan(command);
            } else if (commandWords.length == 4) {
                command = new ScanCommand(stringToIP(commandWords[1]), Integer.parseInt(commandWords[2]));
                scanner.udpScan(command);
            } else if (commandWords.length == 5) {
                command = new ScanCommand(stringToIP(commandWords[1]), Integer.parseInt(commandWords[3]), Integer.parseInt(commandWords[3]));
                scanner.udpScan(command);
            } else if (commandWords.length > 5) {
                List<String> input = Arrays.asList(commandWords);

                int[] ports = input.stream().skip(2).filter(x -> portCheck(x)).mapToInt(Integer::parseInt).toArray();

                command = new ScanCommand(stringToIP(commandWords[1]), ports);

                scanner.udpScan(command);
            } else {
                helper.help(displayHistory, "scanner");
            }
        } else {
            if (commandWords.length == 2) {
                System.out.println("here");
                command = new ScanCommand(stringToIP(commandWords[1]));
                scanner.tcpScan(command);
            } else if (commandWords.length == 3) {
                command = new ScanCommand(stringToIP(commandWords[1]), Integer.parseInt(commandWords[2]));
                scanner.tcpScan(command);
            }else if (commandWords.length == 4) {
                command = new ScanCommand(stringToIP(commandWords[1]), Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]));
                scanner.tcpScan(command);
            } else if (commandWords.length > 4) {
                List<String> input = Arrays.asList(commandWords);

                int[] ports = input.stream().skip(2).filter(x -> portCheck(x)).mapToInt(Integer::parseInt).toArray();

                command = new ScanCommand(stringToIP(commandWords[1]), ports);

                scanner.tcpScan(command);
            } else {
                helper.help(displayHistory, "scanner");
            }
        }
    }

    private void report(String[] commandWords) {

    }

    private void exit() {
        Platform.exit();
    }

    public void printToConsole(String output) {
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    public void displayLiveHosts(HashMap<String, String> results) {
        results.forEach((k, v) -> displayHistory.add(v + " " + k));
        updateOutput();
    }

    public void displayPingResult(String result) {
        displayHistory.add(result);
        updateOutput();
    }

//    public void displayPortResults(HashMap<Integer, String> results) {
//        results.forEach((k, v) -> displayHistory.add("Port " + k + ": " + v));
//        updateOutput();
//        consoleOutput.setScrollTop(Double.MAX_VALUE);
//    }

    public void displayPortResults(Port[] ports) {
        int i = 0;
        for (Port port : ports) {
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
        printToConsole(output);
    }

    private void invalidCommand() {
        String invalid = "Invalid command entered";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += invalid;
        displayHistory.add(invalid);
        printToConsole(output);
    }

    private void invalidIPAddress() {
        String invalid = "Invalid ip entered";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += invalid;
        displayHistory.add(invalid);
        printToConsole(output);
    }

    private void invalidNumberOfArguments() {
        String invalid = "Invalid number of arguments entered";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += invalid;
        displayHistory.add(invalid);
        printToConsole(output);
    }
}
