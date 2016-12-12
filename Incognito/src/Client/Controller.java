package Client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class Controller {

    @FXML
    public MenuItem newSession, exit, clear;

    @FXML
    public Label serverName, hostName, ipAddress, operatingSystem, upTime, macAddress, fireWall, antiVirus;

    public Controller() {

    }

    public void newSession() {

    }

    public void clear() {
        hostName.setText("Host Name: check");
        ipAddress.setText("IP Address:");
        operatingSystem.setText("Operating System:");
        upTime.setText("Up Time:");
        macAddress.setText("Mac Address:");
        fireWall.setText("Firewall Enabled:");
        antiVirus.setText("Anti Virus:");
    }

    public void exitSession() {
        Platform.exit();
    }

}
