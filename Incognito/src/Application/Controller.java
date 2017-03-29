package Application;

import Client.Console;
import Client.GatewayScanner;
import Client.PortScanner;
import Database.Host;
import Database.Port;
import Database.ResultsDatabase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class Controller {

    @FXML
    public MenuItem newSession, exit, clear;

    @FXML
    public TableView displayLiveHosts, displayOpenPorts;

    @FXML
    public TableColumn ipColumn, nameColumn, osColumn, timeColumn;

    @FXML
    public Button displayTopology, refreshHosts, refreshPorts, scan;

    @FXML
    public Label targetHost, hostName, operatingSystem, upTime, macAddress, fireWall;

    @FXML
    public TextField target;

    private ResultsDatabase results;
    private PortScanner scanner;
    private GatewayScanner gatewayScanner;

    public Controller() {
        results = new ResultsDatabase();
        gatewayScanner = new GatewayScanner();
    }

    @FXML
    public void initialize() {
        newSession();
        displayLiveHosts.setPlaceholder(new Label("Network has not been mapped"));
        displayOpenPorts.setPlaceholder(new Label("Ports have not been scanned"));
        scanner = new PortScanner(Config.getConsole());
    }

    private void newSession() {
        clear();
        findGateway();
    }

    public void populateHostsTable() {
        Host[] liveHosts = results.returnHostObjects();

        ObservableList<Host> data = FXCollections.observableArrayList();
        for (Host host : liveHosts) {
            data.add(host);
        }

        displayLiveHosts.setItems(data);

        displayLiveHosts.setRowFactory(tv -> {
            TableRow<Host> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Host rowData = row.getItem();
                    targetHost.setText("Target host: " + rowData.getIP());
                    hostName.setText("Host name: " + rowData.getName());
                    operatingSystem.setText("OS: " + rowData.getOS());
                    populatePortsTable(rowData.getIP());
                }
            });
            return row;
        });
    }

    public void populatePortsTable(String ip) {
        Port[] openPorts = results.returnPortObjects(ip);

        if (openPorts.length < 1) {
            displayOpenPorts.setPlaceholder(new Label("No ports have been found for " + ip));
            return;
        }

        ObservableList<Port> data = FXCollections.observableArrayList();
        for (Port port : openPorts) {
            data.add(port);
        }

        displayOpenPorts.setItems(data);
    }

    public void populatePortsTable() {
        System.out.println("|" + targetHost.getText().substring(13) + "|");

        Port[] openPorts = results.returnPortObjects(targetHost.getText().substring(13));
        System.out.println("how many? " + openPorts.length);

        if (openPorts.length < 1) {
            displayOpenPorts.setPlaceholder(new Label("No ports have been found for " + targetHost.getText()));
        } else {
            ObservableList<Port> data = FXCollections.observableArrayList();
            for (Port port : openPorts) {
                data.add(port);
            }

            displayOpenPorts.setItems(data);
        }
    }

    public void scan() {
        if (targetHost.getText().equals("Target Host:")) {
            displayOpenPorts.setPlaceholder(new Label("Please select a target to scan"));
            return;
        }
        String target = targetHost.getText().substring(13);
        System.out.println("Scanning " + targetHost.getText().substring(13));
        try {
            InetAddress ip = InetAddress.getByName(target);
            System.out.println(ip.getHostAddress());
            scanner.tcpScan(ip, 1, 5000);

        } catch (Exception e) {
            System.out.println("Failed to scan " + target + ": " + e);
        }
    }

    public void displayTopology() {

        //displayTopology.setGraphic(new ImageView("/pathToOpenFileBtnIcon.png"));

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("Topology.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Topology");
            stage.setScene(new Scene(root, 600, 500));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displaySniffer() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("PacketAnalyser.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Packet Sniffer");
            stage.setScene(new Scene(root, 1000, 500));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findGateway() {
        System.out.println("The size: " + gatewayScanner.getGateways().size());
        ArrayList<String> gatewayAddress = gatewayScanner.getGateways();
        if (gatewayAddress.size() > 1) {
            System.out.println("\nWhich gatewayScanner address is correct?");
            for (String address: gatewayAddress) {
                System.out.println(address);
            }
            target.setText(gatewayAddress.get(1));
        } else if (!gatewayAddress.isEmpty()){
            target.setText(gatewayAddress.get(0));
        }
        target.setEditable(false);
    }

    public void clear() {
        targetHost.setText("Target Host:");
        hostName.setText("Host Name:");
        operatingSystem.setText("Operating System:");
        upTime.setText("Up Time:");
        macAddress.setText("Mac Address:");
        fireWall.setText("Firewall Enabled:");
        results.clear();
        populateHostsTable();
        populatePortsTable("");
        displayLiveHosts.setPlaceholder(new Label("Network has not been mapped"));
        displayOpenPorts.setPlaceholder(new Label("Ports have not been scanned"));
    }

    public void exitSession() {
        Platform.exit();
    }

}
