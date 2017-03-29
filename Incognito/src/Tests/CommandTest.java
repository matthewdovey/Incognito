//package Tests;
//
//import Client.Command;
//import Client.Help;
//import Client.Scan;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.TopologyTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
//* Created by mdovey on 13/12/2016.
//*/
//public class CommandTest {
//
//    private static Command testCommand;
//	private static Help testHelp;
//	private static Scan scan;
//	private static List<String> realMainCommands;
//	private static List<String> fakeMainCommands;
//	private static List<String> realHelpCommands;
//	private static List<String> realScanCommands;
//	private static List<String> fakeScanCommands;
//
//    @BeforeClass
//	public static void init(){
//		testCommand = new Command();
//
//		realMainCommands = new ArrayList<>();
//		realMainCommands.add("help");
//		realMainCommands.add("scan");
//		realMainCommands.add("ping");
//		realMainCommands.add("report");
//		realMainCommands.add("clear");
//		realMainCommands.add("exit");
//
//		fakeMainCommands = new ArrayList<>();
//		fakeMainCommands.add("helpp");
//		fakeMainCommands.add(" ");
//		fakeMainCommands.add("");
//		fakeMainCommands.add(null);
//		fakeMainCommands.add("help me");
//
//		realHelpCommands = new ArrayList<>();
//		realHelpCommands.add("help");
//		realHelpCommands.add("help scan");
//		realHelpCommands.add("help ping");
//		realHelpCommands.add("help report");
//		realHelpCommands.add("help clear");
//		realHelpCommands.add("help exit");
//
//		scan = new Scan();
//		realScanCommands = new ArrayList<>();
//		realScanCommands.add("-o");
//		realScanCommands.add("-fw");
//		realScanCommands.add("-av");
//		realScanCommands.add("-p");
//		realScanCommands.add("-pAll");
//		realScanCommands.add("-all");
//
//		fakeScanCommands = new ArrayList<>();
//		fakeScanCommands.add("-");
//		fakeScanCommands.add("-x");
//		fakeScanCommands.add("- o");
//		fakeScanCommands.add("o");
//		fakeScanCommands.add(" -");
//		fakeScanCommands.add(" ");
//		fakeScanCommands.add("");
//
//	}
//
//	@TopologyTest
//	public void testRealMainCommands() {
//		for (String command : realMainCommands) {
//			Assert.assertTrue(testCommand.isCommand(command));
//		}
//	}
//
//	@TopologyTest
//	public void testFakeMainCommands() {
//		for (String command : fakeMainCommands) {
//			Assert.assertFalse(testCommand.isCommand(command));
//		}
//	}
//
////	@TopologyTest
////	public void testHelpCommands() {
////		for (String command : realHelpCommands) {
////			Assert.assertTrue(testHelp.isCommand(command));
////		}
////	}
//
//	@TopologyTest
//	public void testRealScanCommands() {
//		for (String command : realScanCommands) {
//			Assert.assertTrue(scan.isScan(command));
//		}
//	}
//
//	@TopologyTest
//	public void testFakeScanCommands() {
//		for (String command : fakeScanCommands) {
//			Assert.assertFalse(scan.isScan(command));
//		}
//	}
//}