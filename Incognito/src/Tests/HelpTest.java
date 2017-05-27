package Tests;

import Client.Help;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdovey on 04/04/2017.
 */
public class HelpTest extends TestCase {
    private static Help testHelp;
    protected static List<String> realHelpCommands;

    protected void setUp() {
        realHelpCommands = new ArrayList<>();
        realHelpCommands.add("help");
        realHelpCommands.add("help scan");
        realHelpCommands.add("help ping");
        realHelpCommands.add("help report");
        realHelpCommands.add("help clear");
        realHelpCommands.add("help exit");
    }

    //Not sure how to do this as my help methods do not return any values they are all void, and I would rather not force it to return a value
//    @Test
//    public void testHelpCommands() {
//        for (String command : realHelpCommands) {
//            Assert.assertTrue(testHelp.isCommand(command));
//        }
//    }
}
