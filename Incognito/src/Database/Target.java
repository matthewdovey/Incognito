package Database;

/**
 * Created by mdovey on 15/03/2017.
 */
public class Target {

    private Port[] ports;
    private String ip;
    private String name;
    private String os;

    public Target() {

    }

    public Port[] getPorts() {
        return ports;
    }

    public String getIP() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public String getOS() {
        return os;
    }


}
