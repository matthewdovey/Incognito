package Database;

import com.sun.org.apache.xml.internal.utils.StringBufferPool;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;

/**
 * Created by mdovey on 16/03/2017.
 */
public class Packet {

    private SimpleStringProperty flag;
    private SimpleStringProperty protocol;
    private SimpleStringProperty protocolType;
    private SimpleStringProperty source;
    private SimpleStringProperty destination;
    private SimpleStringProperty timestamp;

    public Packet(String flag, String protocol, String protocolType, String source, String destination) {
        this.flag = new SimpleStringProperty(flag);
        this.protocol = new SimpleStringProperty(protocol);
        this.protocolType = new SimpleStringProperty(protocolType);
        this.source = new SimpleStringProperty(source);
        this.destination = new SimpleStringProperty(destination);
        generateTimestamp();
    }

    private void generateTimestamp() {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        this.timestamp = new SimpleStringProperty(stamp.toString());
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

    public void setFlag(String flag) {
        this.flag.set(flag);
    }

    public String getFlag() {
        return this.flag.get();
    }

    public StringProperty flagProperty() {
        return this.flag;
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public String getProtocol() {
        return this.protocol.get();
    }

    public StringProperty protocolProperty() {
        return this.protocol;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType.set(protocolType);
    }

    public String getProtocolType() {
        return this.protocolType.get();
    }

    public StringProperty protocolTypeProperty() {
        return this.protocolType;
    }

    public void setSource(String source) {
        this.source.set(source);
    }

    public String getSource() {
        return this.source.get();
    }

    public StringProperty sourceProperty() {
        return this.source;
    }

    public void setDestination(String destination) {
        this.destination.set(destination);
    }

    public String getDestination() {
        return this.destination.get();
    }

    public StringProperty destinationProperty() {
        return this.destination;
    }
}
