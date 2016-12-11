package sample;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Console extends BorderPane{
    private TextField consoleInput;
    private TextArea consoleOutput;
    private ArrayList<String> consoleHistory;
    private ArrayList<String> displayHistory;
    private ArrayList<String> commands;
    private ArrayList<String> scans;
    private int index = 0;
    private String[] commandWords;

    //TODO: How do I want the output of these commands to be shown....
    //TODO: I guess in the console output window...
    //TODO: How much do I want to allow the user to do?
    //TODO: I think get the basics done, then if there is time make it more advanced...

    public Console() {
        getCommands();
        getScans();
        consoleInput = new TextField();
        consoleOutput = new TextArea();
        consoleHistory = new ArrayList<>();
        displayHistory = new ArrayList<>();

        consoleInput.setStyle("-fx-background-color: #000000;" + "-fx-text-inner-color: white;");;
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
            }
        });
    }

    private void consoleInputHandler(String command) {
        command = command.toLowerCase();
        commandWords = command.split("\\s+");
        displayHistory.add(command);
        if (isCommand(commandWords[0])) {
            consoleHistory.add(0,command);
            updateOutput();
            switch (commandWords[0]) {
                case "help":
                    if (commandWords.length > 2) {
                        displayErrorMessage();
                    } else if (commandWords.length == 2){
                        help(commandWords[1]);
                    } else {
                        helpMenu();
                    }
                    break;
                case "scan":
                    if (commandWords.length == 1) {
                        help("scan");
                    } else {
                        List<String> followingCommands = Arrays.asList(commandWords).subList(1, commandWords.length-1);
                        scan(followingCommands);
                    }
                    break;
                case "clear":
                    consoleOutput.clear();
                    displayHistory.clear();
                    break;
                case "exit":
                    Platform.exit();
            }
        } else {
            displayErrorMessage();
        }
    }

    private void updateOutput() {
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void helpMenu() {
        System.out.println("help");
        String helpMenu = "help - display all valid commands\nclear - clears console of past commands";
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += helpMenu;
        displayHistory.add(helpMenu);
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void help(String filter) {
        System.out.println("help");
        String specificHelp = "";
        switch (filter) {
            case "scan":
                specificHelp = "scan - ";
                break;
            case "clear":
                specificHelp = "clear - clears console of past commands";
                break;
            default:
                helpMenu();
        }
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += specificHelp;
        displayHistory.add(specificHelp);
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void scan(List<String> arguments) {
        boolean proceed = true;
        System.out.println("we get in scan");
        for (String argument : arguments) {
            System.out.println(argument);
            if (!isScan(argument)) {
                displayErrorMessage();
                proceed = false;
                break;
            }
        }
        if (proceed) {
            createScan(arguments);
        }
    }

    private void createScan(List<String> arguments) {
        //TODO: Set up scan with options in arguments, so are they looking for the OS? Anti virus? ETC...
        boolean os, av, fw = false;
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
            }
        }
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

    private boolean isCommand(String command) {
        return commands.contains(command);
    }
    private boolean isScan(String command) {
        System.out.println("we get in isScan");
        System.out.println("isScan command: " + command);
        return scans.contains(command);
    }

    private void getCommands() {
        //TODO: Work out all of the commands that will be available
        commands = new ArrayList<>();
        commands.add("help");
        commands.add("scan");
        commands.add("ping");
        commands.add("clear");
        commands.add("report");
        commands.add("exit");
    }

    private void getScans() {
        //TODO: work out all of the possible scans that wil be available
        scans = new ArrayList<>();
        scans.add("-o");
        scans.add("-fw");
        scans.add("-av");
    }

    private boolean isIP(String ip) {
        //TODO: take arguments after scan and check if it is an IP address if the argument does not match any scan options
        if (ip.isEmpty()) {
            return false;
        }
        String[] segments = ip.split("\\.");
        if (segments.length != 4) {
            return false;
        }
        if (ip.contains("-")) {
            //TODO: I think I need to iterate through all of the segments, check which has the "-"
            //TODO: remember which has and then act accordingly
        }
        for (String segment : segments) {
            if (segment.length() != 3 || Integer.parseInt(segment) > 255 || Integer.parseInt(segment) < 0) {
                displayErrorMessage();
                return false;
            }
        }
        System.out.println("Valid IP address...");
        return true;
    }

    private void produceReport() {
        //TODO: Work out a way of asking the user if they are sure they want to produce a report
        //TODO: and then if "Yes", produce the report..
        //TODO: I could have a boolean and then if input is yes or no while boolean true, then export report....
    }

}
