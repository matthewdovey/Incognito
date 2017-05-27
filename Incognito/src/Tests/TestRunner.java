package Tests;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * Created by mdovey on 04/04/2017.
 */
public class TestRunner extends Runner{
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CommandTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }

    @Override
    public Description getDescription() {
        return null;
    }

    @Override
    public void run(RunNotifier runNotifier) {

    }
}
