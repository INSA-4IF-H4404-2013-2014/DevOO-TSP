package Tests.City;


import Model.City.Arc;
import Model.City.Network;
import Model.City.Node;
import Utils.UtilsException;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Louise
 * Date: 09/12/13
 * Time: 09:03
 * To change this template use File | Settings | File Templates.
 */
public class NetworkTest {
    /**
     * Checks that a complex and valid XML network file is correctly parsed
     */
    @org.junit.Test
    public void testValidCreate() throws UtilsException {
        Network network = Network.createFromXml("resources/tests/plan10x10.xml");

        assertTrue(network.getNodes().size() == 100);
        assertTrue(network.getArcs().size() == 296);

        Node n1 = network.getNodes().get(0);
        assertTrue(n1.getX() == 63);
        assertTrue(n1.getY() == 100);

        Arc a1 = n1.findOutgoingTo(1);
        assertTrue(a1.getStreet().getName().equals("v0"));
        assertTrue(a1.getFrom().getId() == 0);
        assertTrue(a1.getTo().getId() == 1);
        assertTrue(a1.getLength() - 602.1 < 0.000001);
        assertTrue(a1.getSpeed() - 3.9 < 0.000001);

    }

}
