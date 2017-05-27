package Tests;

import Client.Command;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommandTest extends TestCase {

    private Command testCommand;
    private List<String> realMainCommands;
    private List<String> fakeMainCommands;

    protected void setUp() {
        System.out.println("Setting up Command tests...");
        testCommand = new Command();

        realMainCommands = new ArrayList<>();
        realMainCommands.add("help");
        realMainCommands.add("scan");
        realMainCommands.add("ping");
        realMainCommands.add("report");
        realMainCommands.add("clear");
        realMainCommands.add("exit");

        fakeMainCommands = new ArrayList<>();
        fakeMainCommands.add("helpp");
        fakeMainCommands.add(" ");
        fakeMainCommands.add("");
        fakeMainCommands.add(null);
        fakeMainCommands.add("help me");

    }

    @Test
    public void testRealMainCommands() {
        for (String command : realMainCommands) {
            Assert.assertTrue(testCommand.isCommand(command));
        }
    }

    @Test
    public void testFakeMainCommands() {
        for (String command : fakeMainCommands) {
            Assert.assertFalse(testCommand.isCommand(command));
        }
    }
}