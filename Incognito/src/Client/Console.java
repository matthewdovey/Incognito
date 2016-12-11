package sample;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class Console extends BorderPane{
    private TextField consoleInput;
    private TextArea consoleOutput;
    private ArrayList<String> consoleHistory;
    private ArrayList<String> displayHistory;
    private ArrayList<String> commands;
    private int index = 0;
    private String[] commandWords;

    public Console() {
        getCommands();
        consoleInput = new TextField();
        consoleOutput = new TextArea();
        consoleHistory = new ArrayList<>();
        displayHistory = new ArrayList<>();

        consoleInput.setStyle("-fx-background-color: #000000;" + "-fx-text-inner-color: white;");;
        consoleOutput.setEditable(false);
        setCenter(consoleOutput);
        setBottom(consoleInput);

        consoleInput.requestFocus();

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
                        scan(command);
                    }
                    break;
                case "clear":
                    consoleOutput.clear();
                    displayHistory.clear();
                    break;
            }
        } else {
            displayErrorMessage();
        }
    }

    private boolean isCommand(String command) {
        return commands.contains(command);
    }

    private void getCommands() {
        commands = new ArrayList<>();
        commands.add("help");
        commands.add("scan");
        commands.add("ping");
        commands.add("clear");
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
        String specificHelp;
        switch (filter) {
            case "scan":
                specificHelp = "scan - "
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
    }

    private void scan(String... args) {
        System.out.println(args);

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

}
