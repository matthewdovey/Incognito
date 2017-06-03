package Application;

import Client.PacketSniffer;
import Database.Device;
import Database.Packet;
import Database.ResultsDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Arp;

import java.util.ArrayList;

public class PacketAnalyserController {

    private ResultsDatabase results;
    private PacketSniffer packetSniffer;

    @FXML
    TableView displayCapturedPackets;

    public PacketAnalyserController() {
        results = new ResultsDatabase();
        packetSniffer = new PacketSniffer();
    }

    @FXML
    public void initialize() {

    }

    public void start() {

    }


    public void stop() {

    }

    public void newSession() {

    }

    public void save() {

    }

    public void populatePacketTable() {
        Packet[] packets = results.returnPacketObjects();

        if (packets.length < 1) {
            displayCapturedPackets.setPlaceholder(new Label("No packets have been captured"));
        } else {
            ObservableList<Packet> data = FXCollections.observableArrayList();
            for (Packet packet : packets) {
                data.add(packet);
            }

            displayCapturedPackets.setItems(data);
        }
    }
}
