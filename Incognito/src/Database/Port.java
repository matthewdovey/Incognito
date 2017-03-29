package Database;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;

public class Port {

    private SimpleStringProperty ip;
    private SimpleIntegerProperty port;
    private SimpleStringProperty status;
    private SimpleStringProperty timestamp;

    public Port(String ip, int port, String status) {
        this.ip = new SimpleStringProperty(ip);
        this.port = new SimpleIntegerProperty(port);
        this.status = new SimpleStringProperty(status);
        generateTimeStamp();
    }

    private void generateTimeStamp() {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        this.timestamp = new SimpleStringProperty(stamp.toString().substring(11, 19));
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }

    public String getTimestamp() {
        return this.timestamp.get();
    }

    public StringProperty timestampProperty() {
        return this.timestamp;
    }

    public void setIP(String ip) {
        this.ip.set(ip);
    }

    public String getIP() {
        return this.ip.get();
    }

    public StringProperty ipProperty() {
        return this.ip;
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public int getPort() {
        return this.port.get();
    }

    public IntegerProperty portProperty() {
        return this.port;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getStatus() {
        return this.status.get();
    }

    public StringProperty statusProperty() {
        return this.status;
    }
}
