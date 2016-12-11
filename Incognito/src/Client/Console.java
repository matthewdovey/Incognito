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

        if (isCommand(command)) {
            consoleHistory.add(0,command);
            displayHistory.add(command);
            updateOutput();
            switch (command) {
                case "help":
                    System.out.println("help");
                    help();
                    break;
                case "scan":
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

    private void help() {
        System.out.println("help");
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += "help - display all valid commands\nclear - clears command history";
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

    private void displayErrorMessage() {
        String output = "";
        for (String command : displayHistory) {
            output += command + "\n";
        }
        output += "Invalid command entered...";
        displayHistory.add("Invalid command entered...");
        consoleOutput.setText(output);
        consoleOutput.setScrollTop(Double.MAX_VALUE);
    }

}
