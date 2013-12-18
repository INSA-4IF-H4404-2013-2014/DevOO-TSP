package tests.mainwindowcontroller;

import controller.command.AddDelivery;
import controller.MainWindowController;
import model.city.Network;
import model.delivery.Round;
import model.delivery.Schedule;
import utils.UtilsException;
import view.mainwindow.MainWindow;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Louise
 * Date: 15/12/13
 * Time: 21:22
 * To change this template use File | Settings | File Templates.
 */
public class MainWindowControllerTest {

    MainWindowController mwc;
    private Network network;
    private Round round;
    private Schedule schedule;

    public MainWindowControllerTest() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        mwc = new MainWindowController();
        network = Network.createFromXml("resources/tests/round/plan10x10.xml");
        round = Round.createFromXml("resources/tests/round/valid.xml", network);
        schedule = round.getSchedules().get(0);

        MainWindow mc = mwc.getMainWindow();
        mc.setRound(round);
        mc.setNetwork(network);

    }

    /**
     * Checks that doing a command refresh history
     */
    @Test
    public void testDo() throws UtilsException {
        assertTrue(mwc.getHistoryApplied().size() == 0);
        assertTrue(mwc.getHistoryBackedOut().size() == 0);

        String idClient = new String();
        AddDelivery add = new AddDelivery(mwc, idClient, 0, schedule.getEarliestBound(), schedule.getLatestBound());
        mwc.historyDo(add);

        assertTrue(mwc.getHistoryApplied().size() == 1);
        assertTrue(mwc.getHistoryBackedOut().size() == 0);

    }

    /**
     * Checks that undoing a command refresh histories
     */
    @Test
    public void testUndo() throws UtilsException {
        String idClient = new String();
        AddDelivery add = new AddDelivery(mwc, idClient, 0, schedule.getEarliestBound(), schedule.getLatestBound());
        mwc.historyDo(add);

        assertTrue(mwc.getHistoryApplied().size() == 1);
        assertTrue(mwc.getHistoryBackedOut().size() == 0);

        mwc.historyUndo();

        assertTrue(mwc.getHistoryApplied().size() == 0);
        assertTrue(mwc.getHistoryBackedOut().size() == 1);
    }

    /**
     * Checks that redoing a command refresh histories
     */
    @Test
    public void testRedo() throws UtilsException {
        String idClient = new String();
        AddDelivery add = new AddDelivery(mwc, idClient, 0, schedule.getEarliestBound(), schedule.getLatestBound());
        mwc.historyDo(add);
        mwc.historyUndo();

        assertTrue(mwc.getHistoryApplied().size() == 0);
        assertTrue(mwc.getHistoryBackedOut().size() == 1);

        mwc.historyRedo();

        assertTrue(mwc.getHistoryApplied().size() == 1);
        assertTrue(mwc.getHistoryBackedOut().size() == 0);

    }
}
