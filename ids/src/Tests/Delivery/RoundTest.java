package Tests.Delivery;

import Model.City.Network;
import Model.Delivery.Round;
import Model.Delivery.Schedule;
import Utils.UtilsException;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
    /**
     * Checks that a complex and valid XML delivery file is correctly parsed
     */
    @Test
    public void testValidCreate() throws FileNotFoundException {
        try {
            Network network = Network.createFromXml("resources/tests/plan10x10.xml");
            Round round = Round.createFromXml("resources/tests/valid.xml", network);

            assertTrue(round.getWarehouse().getId() == 40);
            assertTrue(round.getSchedules().size() == 3);

            Schedule s1 = round.getSchedules().get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            assertTrue(sdf.format(s1.getEarliestBound().getTime()).equals("08:01:07"));
            assertTrue(sdf.format(s1.getLatestBound().getTime()).equals("12:53:44"));

            assertTrue(s1.getDeliveries().size() == 2);
            assertTrue(s1.getDeliveries().get(0).getId() == 1);
            assertTrue(s1.getDeliveries().get(0).getAddress().getId() == 13);
            assertTrue(s1.getDeliveries().get(0).getClient().getId().equals("611"));
            assertTrue(s1.getDeliveries().get(0).getClient().getDeliveries().size() == 2);
            assertTrue(s1.getDeliveries().get(0).getClient().getDeliveries().contains(s1.getDeliveries().get(0)));
            assertTrue(s1.getDeliveries().get(0).getSchedule() == s1);
        } catch(UtilsException e) {
            fail();
        } catch(ParserConfigurationException e) {
            fail();
        }
    }

    /**
     * Checks that a set of invalid XML delivery files cannot be parsed
     * @throws UtilsException If the network parser fails
     */
    @Test
    public void testInvalidCreate() throws UtilsException, FileNotFoundException {
        Network network = Network.createFromXml("resources/tests/plan10x10.xml");

        for(int i = 1; i < 16; ++i) {
            try {
                System.out.println("Invalid test " + i);
                Round.createFromXml("resources/tests/invalid (" + i + ").xml", network);
                fail();
            } catch(UtilsException e) {
            } catch(ParserConfigurationException e) {
            }
        }
    }


    /**
     * Exports the round contained in resources/tests/valid.xml into resources/tests/export.html
     */
    @Test
    public void testHtmlParser() {
        try {
            Network network = Network.createFromXml("resources/tests/plan10x10.xml");
            Round round = Round.createFromXml("resources/tests/valid.xml", network);

            String html = round.roundToHtml();

            java.io.File file = new java.io.File("resources/tests/export.html");

            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch(IOException e) {
                    e.printStackTrace();
                    fail();
                }
            }

            //System.out.print(html);

            java.io.FileWriter output = new FileWriter(file, false);

            output.write(html);

            output.close();
        } catch(Exception e) {
            fail();
        }
    }
}
