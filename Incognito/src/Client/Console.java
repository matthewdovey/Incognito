package Client;
import Application.Config;
import Database.Port;
import javafx.application.Platform;
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
    private NetworkMapper map;

    //TODO: How do I want the output of these commands to be shown....
    //TODO: I guess in the console output window...
    //TODO: How much do I want to allow the user to do?
    //TODO: I think get the basics done, then if there is time make it more advanced...

    public Console() {
        scan = new PortScanner(this);
        helper = new Help();
        report = new Report();
        command = new Command();
        map = new NetworkMapper(this);

        Config.setConsole(this);

        consoleInput = new TextField();
        consoleOutput = new TextArea();
        consoleHistory = new ArrayList<>();
        displayHistory = new ArrayList<>();

        consoleInput.setStyle("-fx-background-color: #000000;" + "-fx-text-inner-color: white;");
        consoleOutput.setEditable(false);
        setCenter(consoleOutput);
        setBottom(consoleInput);

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
                        //add each thread into a list, if ctrl-c is clicked iterate through list and shutdown threads...
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
                    help(commandWords);
                    break;
                case "scan":
                    scan(commandWords);
                    break;
                case "clear":
                    clear();
                    break;
                case "ping":
                    ping(commandWords);
                    break;
                case "report":
                    report(commandWords);
                    break;
                case "map":
                    mapper(commandWords);
                    break;
                case "exit":
                    exit(commandWords);
                    break;
            }
        } else {
            displayErrorMessage();
        }
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

    private void mapper(String[] commandWords) {
        ExecutorService service = Executors.newFixedThreadPool(1);

        if (commandWords.length < 2 || commandWords.length > 6) {
            helper.help(displayHistory, "map");
        } else if (!isIP(commandWords[1])) {
            displayPingResult("Invalid IP address...");
        } else if (commandWords.length == 2) {
                if (isIP(commandWords[1])) {

                    Future future = service.submit(new Callable(){
                        public Object call() throws Exception {
                            try {
                                map.pingCheck(commandWords[1], 1, 255);
                            } catch (IOException e) {

                            }
                            return "Ping check Failed";
                        }
                    });

                    try {
                        System.out.println("future.get() = " + future.get());
                    } catch (Exception e) {

                    }

                    service.shutdown();
                }

        } else if (commandWords.length == 4) {
            if (isIP(commandWords[1])) {
                if (rangeCheck(commandWords[2]) && rangeCheck(commandWords[3])) {

                    Future future = service.submit(new Callable(){
                        public Object call() throws Exception {
                            try {
                                return map.pingCheck(commandWords[1], Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]));
                            } catch (IOException e) {

                            }
                            return "Ping check failed";
                        }
                    });

                    try {
                        System.out.println("future.get() = " + future.get());
                    } catch (Exception e) {

                    }

                    Thread tcpThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                map.tcpCheck(commandWords[1], Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]));
                            } catch (IOException e) {

                            }
                        }
                    });

                    service.submit(tcpThread);

                    service.shutdown();

                    try {
                        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                        System.out.println("all finished...");
                    } catch (InterruptedException e) {

                    }
                } else {
                    //Output error message about range...
                }
            }
        } else if (commandWords.length <= 6) {


        } else {
            helper.help(displayHistory, "mapper");
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
                        try {
                            map.pingCheck(commandWords[1], Integer.parseInt(commandWords[2]), Integer.parseInt(commandWords[3]));
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    } else {
                        //Output invalid command issued...
                    }
                }
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

    private boolean rangeCheck(String number) {
        if (Integer.parseInt(number) > 0 && Integer.parseInt(number) <= 255) {
            return true;
        }
        return false;
    }
}
