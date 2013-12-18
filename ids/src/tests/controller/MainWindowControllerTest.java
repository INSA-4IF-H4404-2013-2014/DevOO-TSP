package tests.controller;

import controller.MainWindowController;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: nicolas
 * Date: 11/12/13
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
public class MainWindowControllerTest {

    @Test
    public void testComputeRound() {
        MainWindowController main = new MainWindowController();

        main.loadNetwork();
        main.loadRound();

        try {
            main.computeRound(main.getMainWindow().getNetwork(), main.getMainWindow().getRound());
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
