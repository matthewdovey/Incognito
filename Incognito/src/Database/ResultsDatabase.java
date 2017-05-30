package Database;

import java.sql.*;
import java.util.HashMap;

public class ResultsDatabase {

    private Connection connection;

    public ResultsDatabase() {

    }

    public void createDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("DROP TABLE if EXISTS hosts");
            statement.executeUpdate("CREATE TABLE hosts (id INT NOT NULL, ip TEXT NOT NULL, host TEXT NOT NULL, os TEXT NOT NULL, stamp TEXT NOT NULL, UNIQUE (ip))");

            statement.executeUpdate("DROP TABLE if EXISTS ports");
            statement.executeUpdate("CREATE TABLE ports (id INT NOT NULL, ip TEXT NOT NULL, port INT NOT NULL , status TEXT NOT NULL, stamp TEXT NOT NULL, UNIQUE (port))");

            statement.executeUpdate("DROP TABLE if EXISTS packets");
            statement.executeUpdate("CREATE TABLE packets (id INT NOT NULL, flag TEXT NOT NULL, protocol TEXT NOT NULL, protocolType TEXT NOT NULL, source TEXT NOT NULL, destination TEXT NOT NULL, stamp TEXT NOT NULL)");

            statement.executeUpdate("DROP TABLE if EXISTS devices");
            statement.executeUpdate("CREATE TABLE devices (id INT NOT NULL, device TEXT NOT NULL, description TEXT NOT NULL, hardwareAddress TEXT NOT NULL, UNIQUE (device))");

            connection.close();
        } catch (Exception e) {

        }
    }

    public void saveLiveHostResults(Host[] hosts) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            String sql = "INSERT INTO hosts VALUES (?, ?, ?, ?, ?)";
            for (Host host : hosts) {
                PreparedStatement prep = connection.prepareStatement(sql);
                prep.setInt(1, 1);
                prep.setString(2, host.getIP());
                prep.setString(3, host.getName());
                prep.setString(4, host.getOS());
                prep.setString(5, host.getTimestamp());
                prep.executeUpdate();
                prep.close();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePortScanResults(Port[] ports) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            String sql = "INSERT INTO ports VALUES (?, ?, ?, ?, ?)";
            int id = 0;
            for (Port port : ports) {
                PreparedStatement prep = connection.prepareStatement(sql);
                prep.setInt(1, id++);
                prep.setString(2, port.getIP());
                prep.setInt(3, port.getPort());
                prep.setString(4, port.getStatus());
                prep.setString(5, port.getTimestamp());
                prep.executeUpdate();
                prep.close();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePacketResults(Packet packet) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            String sql = "INSERT INTO packets VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, 1);
            prep.setString(2, packet.getFlag());
            prep.setString(3, packet.getProtocol());
            prep.setString(4, packet.getProtocolType());
            prep.setString(5, packet.getSource());
            prep.setString(6, packet.getDestination());
            prep.setString(7, packet.getTimestamp());
            prep.executeUpdate();
            prep.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDevices(Device[] devices) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            String sql = "INSERT INTO devices VALUES (?, ?, ?, ?)";
            int id = 0;
            for (Device device : devices) {
                PreparedStatement prep = connection.prepareStatement(sql);
                prep.setInt(1, id++);
                prep.setString(2, device.getName());
                prep.setString(3, device.getDescription());
                prep.setBytes(4, device.getHardwareAddress());
                prep.executeUpdate();
                prep.close();
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Host[] returnHostObjects() {
        int index = 0;
        Host[] hosts = {};
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from hosts");
            while(rs.next()) {
                index++;
            }
            hosts = new Host[index];
            rs = statement.executeQuery("select * from hosts");
            index = 0;
            Host host;
            while(rs.next())
            {
                host = new Host(rs.getString("ip"), rs.getString("host"), rs.getString("os"));
                host.setTimestamp(rs.getString("stamp"));
                hosts[index] = host;
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hosts;
    }

    public Port[] returnPortObjects(String ip) {
        int index = 0;
        Port[] ports = {};
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");

            String sql = "SELECT * FROM ports WHERE ip = ?";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, ip);

            ResultSet rs = prep.executeQuery();

            while(rs.next()) {
                index++;
            }
            ports = new Port[index];

            rs = prep.executeQuery();

            index = 0;
            Port port;
            while(rs.next())
            {
                port = new Port(rs.getString("ip"), rs.getInt("port"), rs.getString("status"));
                port.setTimestamp(rs.getString("stamp"));
                ports[index] = port;
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ports;
    }

    public Packet[] returnPacketObjects() {
        int index = 0;
        Packet[] packets = {};
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM packets");
            while(rs.next()) {
                index++;
            }
            packets = new Packet[index];
            rs = statement.executeQuery("SELECT * FROM packets");
            index = 0;
            Packet packet;
            while(rs.next())
            {
                packet = new Packet(rs.getString("flag"), rs.getString("protocol"), rs.getString("protocolType"), rs.getString("source"), rs.getString("destination"));
                packet.setTimestamp(rs.getString("stamp"));
                packets[index] = packet;
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packets;
    }

    public Device[] returnDeviceObjects() {
        int index = 0;
        Device[] devices = {};
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM devices");
            while(rs.next()) {
                index++;
            }
            devices = new Device[index];
            rs = statement.executeQuery("SELECT * FROM devices");
            index = 0;
            while(rs.next())
            {
                devices[index] = new Device(rs.getString("device"), rs.getString("description"), rs.getBytes("hardwareAddress"));
                index++;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }

    public void clear() {
        createDatabase();
    }


    //    public HashMap<String, String> getValueFromTable(String table, String value) {
//        HashMap<String, String> values = new HashMap<>();
//        try {
//            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
//            Statement statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery("select * from " + table);
//            while (rs.next()) {
//                //values.put(rs.getString(rs.ge))
//            }
//        } catch (Exception e) {
//
//        }
//        return null;
//    }
}
