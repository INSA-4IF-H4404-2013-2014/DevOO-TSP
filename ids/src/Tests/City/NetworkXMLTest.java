package Tests.City;


import Model.City.Arc;
import Model.City.Network;
import Model.City.Node;
import Model.Delivery.Round;
import Utils.UtilsException;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Louise
 * Date: 09/12/13
 * Time: 09:03
 * To change this template use File | Settings | File Templates.
 */
public class NetworkXMLTest {
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

        Arc a2 = a1.getStreet().getArcs().get(0);
        assertTrue(a2.getStreet().getName().equals("v0"));

    }

    private void testInvalidCreate(String xmlPath) {
        final String testDir = "resources/tests/network/";

        boolean success = false;

        try {
            Network.createFromXml(testDir + xmlPath);
            System.out.println("unexpected success");
        } catch(UtilsException e) {
            success = true;
        } catch(Exception e) {
            System.out.println("unexpected exception");
        }

        if(!success) {
            fail("test " + xmlPath + " has failed");
        }
    }

    /**
     * Tests all xml
     * @throws UtilsException
     */
    @Test
    public void testInvalidCreateAll() {
        testInvalidCreate("networkInvalidSyntax0.xml");
        testInvalidCreate("networkInvalidSyntax1.xml");
        testInvalidCreate("networkInvalidRoot.xml");
        testInvalidCreate("networkMissingAttrDestination.xml");
        testInvalidCreate("networkMissingAttrLongueur.xml");
        testInvalidCreate("networkMissingAttrNomRue.xml");
        testInvalidCreate("networkMissingAttrVitesse.xml");
        testInvalidCreate("networkMissingAttrX.xml");
        testInvalidCreate("networkMissingAttrY.xml");
        testInvalidCreate("networkNegaviteAttrLongueur.xml");
        testInvalidCreate("networkNegaviteAttrVitesse.xml");
        testInvalidCreate("networkWrongAttrLongueur.xml");

        System.out.println("testInvalidCreateAll: OK");
    }

}
