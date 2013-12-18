package Tests.Delivery;

import Model.City.Arc;
import Model.City.Network;
import Model.City.Node;
import Model.Delivery.Delivery;
import Model.Delivery.Round;
import Model.Delivery.Schedule;
import Tests.City.NetworkTest;
import Utils.UtilsException;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Deeper
 * Date: 07/12/13
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */
public class RoundTest {

    private Network network;
    private Round round;
    private List<Schedule> schedules;

    /**
     * Constructor
     */
    public RoundTest() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        network = Network.createFromXml("resources/tests/round/plan10x10.xml");
        round = Round.createFromXml("resources/tests/round/valid.xml", network);
        schedules = round.getSchedules();
    }

    /**
     * Checks that addition of a delivery changes the next delivery's ID and adds this delivery in the list
     */
    @org.junit.Test
    public void testAdd() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        String idClient = new String();

        assertTrue(schedules.get(0).getNextDeliveryId() == 3);
        assertTrue(schedules.get(0).getDeliveries().size() == 2);

        round.addDelivery(idClient, 5, schedules.get(0).getEarliestBound(), schedules.get(0).getLatestBound());

        assertTrue(schedules.get(0).getNextDeliveryId() == 4);
        assertTrue(schedules.get(0).getDeliveries().size() == 3);
        assertTrue(schedules.get(0).getDeliveries().get(2).getAddress().getId() == 5);
    }

    /**
     * Checks that deletion of a delivery removes this delivery in the list
     */
    @org.junit.Test
    public void testRemove() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        String idClient = new String();
        round.addDelivery(idClient, 2, schedules.get(0).getEarliestBound(), schedules.get(0).getLatestBound());

        assertTrue(schedules.get(0).getNextDeliveryId() == 4);
        assertTrue(schedules.get(0).getDeliveries().size() == 3);

        round.removeDelivery(2);

        assertTrue(schedules.get(0).getNextDeliveryId() == 4);
        assertTrue(schedules.get(0).getDeliveries().size() == 2);
    }



}
