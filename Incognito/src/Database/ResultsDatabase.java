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
            statement.executeUpdate("CREATE TABLE devices (id INT NOT NULL, device TEXT NOT NULL, description TEXT NOT NULL, UNIQUE (device))");

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
            System.out.println("Failed to save hosts to database...");
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
            System.out.println("Failed to save ports to database...");
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
            System.out.println("Failed to save packet to database...");
        }
    }

    public void saveDevices(Device[] devices) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            String sql = "INSERT INTO devices VALUES (?, ?, ?)";
            int id = 0;
            for (Device device : devices) {
                System.out.println("for");
                PreparedStatement prep = connection.prepareStatement(sql);
                prep.setInt(1, id++);
                prep.setString(2, device.getName());
                prep.setString(3, device.getDescription());
                prep.executeUpdate();
                prep.close();
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Failed to save devices to database...");
        }
    }

//    public void saveDevices(int id, Device device) {
//        System.out.println("ID: " + id + " Device: " + device.getName() + " Description: " + device.getDescription());
//        try {
//            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
//            String sql = "INSERT INTO devices VALUES (?, ?, ?)";
//            PreparedStatement prep = connection.prepareStatement(sql);
//            prep.setInt(1, 1);
//            prep.setString(2, device.getName());
//            prep.setString(3, device.getDescription());
//            prep.executeUpdate();
//            prep.close();
//            connection.close();
//        } catch (Exception e) {
//            System.out.println("Failed to save device to database...");
//        }
//    }

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
            System.out.println("Failed to return hosts from database...");
        }
        return hosts;
    }

    public Port[] returnPortObjects(String ip) {
        System.out.println("Attempting to return port objects...");
        System.out.println(ip);
        int index = 0;
        Port[] ports = {};
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            System.out.println("Connection established...");

            String sql = "SELECT * FROM ports WHERE ip = ?";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, ip);

            ResultSet rs = prep.executeQuery();

            System.out.println("statement created...");
            while(rs.next()) {
                index++;
            }
            System.out.println("while complete, index :" + index);
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
            System.out.println("closing connection");
            connection.close();
        } catch (Exception e) {
            System.out.println("Failed to return ports from database...");
        }
        return ports;
    }

    public Packet[] returnPacketObjects() {
        System.out.println("Attempting to return packet objects...");
        int index = 0;
        Packet[] packets = {};
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            Statement statement = connection.createStatement();
            System.out.println("Connection established...");
            ResultSet rs = statement.executeQuery("SELECT * FROM packets");
            while(rs.next()) {
                index++;
            }
            System.out.println("while complete, index :" + index);
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
            System.out.println("Failed to return packets from database...");
        }
        return packets;
    }

    public Device[] returnDeviceObjects() {
        displayTable("hosts");
        displayTable("ports");
        displayTable("devices");
        int index = 0;
        Device[] devices = {};
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            Statement statement = connection.createStatement();
            System.out.println("Connection established...");
            ResultSet rs = statement.executeQuery("SELECT * FROM devices");
            while(rs.next()) {
                index++;
            }
            System.out.println("Finished first while");
            devices = new Device[index];
            rs = statement.executeQuery("SELECT * FROM devices");
            index = 0;
            while(rs.next())
            {
                devices[index] = new Device(rs.getString("device"), rs.getString("description"));
                index++;
            }
            System.out.println("finished second while");
            connection.close();
        } catch (Exception e) {
            System.out.println("Failed to return devices from database...");
        }
        return devices;
    }

    public HashMap<String, String> getValueFromTable(String table, String value) {
        HashMap<String, String> values = new HashMap<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:results.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from " + table);
            while (rs.next()) {
                //values.put(rs.getString(value))
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void displayTable(String table) {
        System.out.println("");
        System.out.println("Display Table : " + table);
        if (table.equals("hosts")) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:results.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from " + table);
                while(rs.next())
                {
                    System.out.print(rs.getInt("id") + " | ");
                    System.out.print(rs.getString("ip"));
                    System.out.print(" | host: " + rs.getString("host"));
                    System.out.print(" | os: " + rs.getString("os") + " | ");
                    System.out.println(" timestamp: " + rs.getString("stamp"));
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Failed to display hosts table...");
            }
        } else if (table.equals("devices")) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:results.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from " + table);
                while(rs.next())
                {
                    System.out.print(rs.getInt("id") + " | ");
                    System.out.print(rs.getString("device"));
                    System.out.println(" | Desc: " + rs.getString("description"));
                }
                connection.close();
            } catch (Exception e) {

            }
        } else {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:results.db");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from " + table);
                while(rs.next())
                {
                    System.out.print(rs.getInt("id") + " | ");
                    System.out.print(rs.getString("ip"));
                    System.out.print(" | port: " + rs.getInt("port"));
                    System.out.println(" | (" + rs.getString("status") + ") |");
                }
                connection.close();
            } catch (Exception e) {
                System.out.println("Failed to open database...");
            }
        }
    }

    public void clear() {
        createDatabase();
    }

    public static void main(String[] args) {
        ResultsDatabase results = new ResultsDatabase();

        Device[] devices = new Device[4];
        devices[0] = new Device("gay", "gayworked1");
        devices[1] = new Device("lol", "gayworked2");
        devices[2] = new Device("hey", "gayworked3");
        devices[3] = new Device("not", "gayworked4");

        results.saveDevices(devices);

        results.displayTable("devices");

    }
}
