package Database;

/**
 * Created by Matthew on 18/03/2017.
 */
public class Device {

    private String name, description;

    public Device(String name, String description) {
        this.name = name;
        this.description = description;
        System.out.println();
        System.out.println(name);
        System.out.println(description);
        System.out.println();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
