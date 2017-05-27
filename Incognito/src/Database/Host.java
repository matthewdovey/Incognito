package Database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.net.InetAddress;
import java.sql.Timestamp;

public class Host {

    private SimpleStringProperty ip;
    private SimpleStringProperty os;
    private SimpleStringProperty name;
    private SimpleStringProperty timestamp;

    public Host(String ip, String name) {
        this.ip = new SimpleStringProperty(ip);
        this.name = new SimpleStringProperty(name);
        this.os = new SimpleStringProperty("unknown");
        generateTimeStamp();
    }

    public Host(String ip, String name, String os) {
        this.ip = new SimpleStringProperty(ip);
        this.name = new SimpleStringProperty(name);
        this.os = new SimpleStringProperty(os);
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

    public void setName(String name) {
        this.name.set(name);
    }

    public String getName() {
        return this.name.get();
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public void setOS(String os) {
        this.os.set(os);
    }

    public String getOS() {
        return this.os.get();
    }

    public StringProperty osProperty(){
        return this.os;
    }
}

