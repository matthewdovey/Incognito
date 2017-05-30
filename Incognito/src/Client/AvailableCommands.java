package Client;

import java.util.ArrayList;

public class AvailableCommands {

    private ArrayList<String> commands;

    public AvailableCommands() {
        getCommands();
    }

    private void getCommands() {
        commands = new ArrayList<>();
        commands.add("help");
        commands.add("scan");
        commands.add("ping");
        commands.add("map");
        commands.add("clear");
        commands.add("report");
        commands.add("exit");
    }

    public boolean isCommand(String command) {
        return commands.contains(command);
    }
}
