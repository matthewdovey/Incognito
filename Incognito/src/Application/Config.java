package Application;

import Client.Console;

public class Config {

    private static Console mainConsole;
    private static int chosenDevice;
    private static StringBuilder errbuf;

    public Config() {

    }

    public static void setConsole(Console console) {
        mainConsole = console;
        System.out.println("Console set!");
    }

    public static Console getConsole() {
        System.out.println("Console Retrieved!");
        return mainConsole;
    }

    public static void setDevice(int device) {
        chosenDevice = device;
        System.out.println("Device set as " + device);
    }

    public static int getDevice() {
        return chosenDevice;
    }

    public static void setErrbuf(StringBuilder error) {
        errbuf = error;
    }

    public static StringBuilder getErrbuf() {
        return errbuf;
    }
}
