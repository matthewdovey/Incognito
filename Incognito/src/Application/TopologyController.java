package Application;

import Database.Host;
import Database.ResultsDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class TopologyController {

    @FXML
    public HBox topRow, gateway, bottomRow;

    private ResultsDatabase result;

    public TopologyController() {
        result = new ResultsDatabase();
    }

    @FXML
    public void initialize() {
        //test();
        generateTopology(result.returnHostObjects());
    }

    private void generateTopology(Host[] hosts) {
        Image image;
        ImageView icon;

        if (hosts.length == 0) {
            gateway.getChildren().add(new Label("No live hosts have been found..."));
        } else {
            int half = (hosts.length / 2);
            System.out.println(half);
            int spacing = 150 / half;

            topRow.setSpacing(spacing);
            bottomRow.setSpacing(spacing);

            System.out.println("Half: " + half);
            int index = 0;
            for (Host host : hosts) {
                System.out.println("IP: " + host.getIP());
                if (host.getIP().equals("192.168.0.1")) {
                    System.out.println("should have a gateway");
                    image = new Image("/Graphics/gateway.png");
                    gateway.getChildren().add(generatePane(image, host.getIP(), host.getOS(),0));
                    //re-write to check for a 1 as the last segment of the ip address
                } else {
                    index++;
                    image = new Image("/Graphics/computer.png");
                    if (index <= half) {
                        topRow.getChildren().add(generatePane(image, host.getIP(), host.getOS(), hosts.length));
                    } else {
                        bottomRow.getChildren().add(generatePane(image, host.getIP(), host.getOS(), hosts.length));
                    }
                }
            }
        }
    }

    private Pane generatePane(Image image, String ip, String os, int hosts) {
        int size = 0;
        if (hosts > 0) {
            size = hosts / 8;
        }
        Pane pane = new Pane();
        ImageView icon = new ImageView(image);
        icon.setFitHeight(50);
        icon.setFitWidth(50);

        if (size > 0) {
            icon.setFitHeight(50/size);
            icon.setFitWidth(50/size);
        }

        Label ipLabel = new Label(ip);
        Label osLabel = new Label(os);

        ipLabel.setTranslateY(55);
        ipLabel.setTranslateX(-5);
        osLabel.setTranslateY(70);
        osLabel.setTranslateX(5);

        ipLabel.setFont(new Font("Arial", 10));
        osLabel.setFont(new Font("Arial", 10));

        pane.getChildren().add(icon);
        pane.getChildren().add(ipLabel);
        pane.getChildren().add(osLabel);
        return pane;
    }

    public void test() {
        Host[] hosts = new Host[5];
        hosts[0] = new Host("192.168.0.1","gateway");
        hosts[1] = new Host("192.168.0.2","matthew");
        hosts[2] = new Host("192.168.0.3","ayuub");
        hosts[3] = new Host("192.168.0.4","masud");
        hosts[4] = new Host("192.168.0.5","chip");
        generateTopology(hosts);
    }
}
