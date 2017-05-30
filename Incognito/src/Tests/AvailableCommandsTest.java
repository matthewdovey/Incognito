package Tests;

import Client.AvailableCommands;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AvailableCommandsTest extends TestCase {

    private AvailableCommands testAvailableCommands;
    private List<String> realMainCommands;
    private List<String> fakeMainCommands;

    protected void setUp() {
        System.out.println("Setting up Command tests...");
        testAvailableCommands = new AvailableCommands();

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
            Assert.assertTrue(testAvailableCommands.isCommand(command));
        }
    }

    @Test
    public void testFakeMainCommands() {
        for (String command : fakeMainCommands) {
            Assert.assertFalse(testAvailableCommands.isCommand(command));
        }
    }
}