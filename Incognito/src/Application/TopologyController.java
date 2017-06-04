package Application;

import Client.NetworkTopology;
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

    private NetworkTopology topology;

    public TopologyController() {
        topology = new NetworkTopology();
    }

    @FXML
    public void initialize() {
        topology.generateTopology(topRow, gateway, bottomRow);
    }
}
