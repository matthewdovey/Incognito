package Application;

import Database.Device;
import Database.ResultsDatabase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DeviceController {

    @FXML
    VBox listOfDevices;

    private ResultsDatabase results;
    private int chosenDevice;

    public DeviceController() {
        results = new ResultsDatabase();
        chosenDevice = 0;
    }

    public void initialize() {
        System.out.println("Initialised!!!!");
        showDevices();
    }

    private void showDevices() {

        Device[] devices = results.returnDeviceObjects();

        System.out.println("Length: " + devices.length);

        System.out.println("Normal: " + 150 / devices.length);
        double height = Math.floor(150 / devices.length);
        System.out.println("Height: " + height);
        Button deviceButton;

        int deviceNumber = 0;
        for (Device device : devices) {
            deviceNumber++;
            final int devNum = deviceNumber;
            System.out.println("for");
            deviceButton = new Button(device.getName());
            deviceButton.setMinHeight(height);
            deviceButton.setMinWidth(340);
            deviceButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setDevice(devNum);
                }
            });
            System.out.println("trying to add...");
            listOfDevices.getChildren().add(deviceButton);
        }
        System.out.println("done");
    }

    public void cancelSelection() {
        Stage stage = (Stage) listOfDevices.getScene().getWindow();
        stage.close();
    }

    public void setDevice(int num) {
        chosenDevice = num;
        System.out.println(num);
    }

    public void ok() {
        Config.setDevice(chosenDevice);
        Stage stage = (Stage) listOfDevices.getScene().getWindow();
        stage.close();
    }
}
