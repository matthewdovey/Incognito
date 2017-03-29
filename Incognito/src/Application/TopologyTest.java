package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Mdovey on 11/03/2017.
 */
public class TopologyTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("Topology.fxml"));
            Stage testStage = new Stage();
            testStage.setTitle("Network Topology");
            testStage.setScene(new Scene(root, 600, 500));
            testStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
