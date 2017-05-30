package Database;

/**
 * Created by Matthew on 18/03/2017.
 */
public class Device {

    private final String name, description;
    private final byte[] hardwareAddress;

    public Device(String name, String description, byte[] hardwareAddress) {
        this.name = name;
        this.description = description;
        this.hardwareAddress = hardwareAddress;
        System.out.println();
        System.out.println("Name: " + name + "     Description: " + description + "       Hardware Address: " + hardwareAddress);
        System.out.println();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public byte[] getHardwareAddress() { return this.hardwareAddress; }
}
