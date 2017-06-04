package Client;

import Application.Config;
import Database.Device;
import Database.Packet;
import Database.ResultsDatabase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Arp;

import java.util.ArrayList;

public class PacketSniffer {

    private ResultsDatabase results;
    private Packet[] packets;

    public PacketSniffer() {
        results = new ResultsDatabase();
    }

    public void sniff(int numberOfPackets) {
        packets = new Packet[numberOfPackets];
        try {

            // Will be filled with NICs
            ArrayList<PcapIf> alldevs = new ArrayList<>();

            // For any error msgs
            StringBuilder errbuf = new StringBuilder();

            //Getting a list of devices
            int r = Pcap.findAllDevs(alldevs, errbuf);
            System.out.println("R: " + r);

            if (r != Pcap.OK) {
                return;
            }

            Device[] devices = new Device[alldevs.size()];

            if (Config.getDevice() == 0) {

                String description;
                int count = 0;

                for (PcapIf device : alldevs) {
                    description = (device.getDescription() != null) ? device.getDescription() : "No description available";
                    System.out.println(device.getAddresses());
                    devices[count] = new Device(device.getName(), description, device.getHardwareAddress());
                    count++;
                }

                results.saveDevices(devices);

                Parent root;
                root = FXMLLoader.load(getClass().getResource("Device.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Devices");
                stage.setScene(new Scene(root, 400, 300));
                stage.show();
            } else {
                PcapIf device = alldevs.get(Config.getDevice()-1);

                int snaplen = 64 * 1024;           // Capture all packets, no trucation
                int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
                int timeout = 10 * 1000;           // 10 seconds in millis

                //Open the selected device to capture packets
                Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

                if (pcap == null) {
                    return;
                }

                //Create packet handler which will receive packets
                PcapPacketHandler jpacketHandler = new PcapPacketHandler() {
                    Arp arp = new Arp();

                    @Override
                    public void nextPacket(PcapPacket pcapPacket, Object o) {
                        //Here i am capturing the ARP packets only,you can capture any packet that you want by just changing the below if condition
                        if (pcapPacket.hasHeader(arp)) {
                            recordPacket(new Packet(arp.getName(), Integer.toString(arp.protocolType()), arp.protocolTypeDescription(), "192.168.100.144", "192.168.100.1"));
                        }
                    }
                };
                //we enter the loop and capture the 10 packets here.You can  capture any number of packets just by changing the first argument to pcap.loop() function below
                pcap.loop(numberOfPackets, jpacketHandler, "jnetpcap rocks!");
                //Close the pcap
                pcap.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordPacket(Packet packet) {
        results.savePacketResults(packet);
    }
}
