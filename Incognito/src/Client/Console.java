import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

/**
 * Created by mdovey on 07/12/2016.
 */
public class Console {
    private TextField consoleInput;
    private TextArea consoleOutput;
    private ArrayList<String> consoleHistory;

    public Console() {
        consoleInput = new TextField();
        consoleOutput = new TextArea();
        consoleHistory = new ArrayList<String>();

        consoleOutput.setEditable(false);

        consoleInput.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                consoleInputHandler(consoleInput.getText());
            }
        });
    }

    private void consoleInputHandler(String command) {
        command = command.toLowerCase();
        if (inputCheck(command)) {
            consoleHistory.add(command);
            if (command == "help") {
                help();
            }
        }
    }

    private boolean inputCheck(String command) {
        if (command == null) {
            return false;
        }
        //Do I want to allow users to enter blank lines??? to clean up the console a bit
        if (command.length() > 0) {
            return false;
        }
        return true;
    }

    private void help() {
        System.out.println("help");
        //check the diferences between the two
        consoleOutput.insertText(1,"help");
        consoleOutput.setText("help");
    }

}
