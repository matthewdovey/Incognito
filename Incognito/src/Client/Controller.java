package Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class Controller {

    @FXML
    public MenuItem newSession, exit, clear;

    public Controller() {

    }

    public void exitSession(ActionEvent event) {
        Platform.exit();
    }

}
