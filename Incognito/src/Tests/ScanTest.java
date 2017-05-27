package Tests;

import Client.NetworkMapper;
import Client.Scan;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdovey on 04/04/2017.
 */
public class ScanTest extends TestCase {

    private Scan scan;
    private List<String> realScanCommands;
    private List<String> fakeScanCommands;

    protected void setUp() {
        scan = new Scan();

        realScanCommands = new ArrayList<>();
        realScanCommands.add("-o");
        realScanCommands.add("-fw");
        realScanCommands.add("-av");
        realScanCommands.add("-p");
        realScanCommands.add("-pAll");
        realScanCommands.add("-all");

        fakeScanCommands = new ArrayList<>();
        fakeScanCommands.add("-");
        fakeScanCommands.add("-x");
        fakeScanCommands.add("- o");
        fakeScanCommands.add("o");
        fakeScanCommands.add(" -");
        fakeScanCommands.add(" ");
        fakeScanCommands.add("");
    }

    @Test
    public void testRealScanCommands() {
        for (String command : realScanCommands) {
            Assert.assertTrue(scan.isScan(command));
        }
    }

    @Test
    public void testFakeScanCommands() {
        for (String command : fakeScanCommands) {
            Assert.assertFalse(scan.isScan(command));
        }
    }
}
