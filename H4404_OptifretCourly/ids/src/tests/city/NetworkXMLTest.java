package tests.city;


import model.city.Arc;
import model.city.Network;
import model.city.Node;
import utils.UtilsException;
import org.junit.Test;

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

    private void testInvalidCreate(String xmlPath, boolean supposedToSuccess) {
        final String testDir = "resources/tests/network/";

        try {
            Network.createFromXml(testDir + xmlPath);

            if(supposedToSuccess) {
                return;
            }

            System.out.println("unexpected success");
        } catch(UtilsException e) {
            if(!supposedToSuccess) {
                return;
            }

            System.out.println("exception");
        } catch(Exception e) {
            System.out.println("unexpected exception");
        }

        fail("test " + xmlPath + " has failed");
    }

    /**
     * tests all xml
     * @throws UtilsException
     */
    @Test
    public void testInvalidCreateAll() {
        testInvalidCreate("networkWorking.xml", true);

        testInvalidCreate("networkNonExisting.xml", false);
        testInvalidCreate("networkEmpty.xml", false);
        testInvalidCreate("networkInvalidSyntax0.xml", false);
        testInvalidCreate("networkInvalidSyntax1.xml", false);
        testInvalidCreate("networkInvalidRoot.xml", false);
        testInvalidCreate("networkMissingAttrDestination.xml", false);
        testInvalidCreate("networkMissingAttrLongueur.xml", false);
        testInvalidCreate("networkMissingAttrNodeId.xml", false);
        testInvalidCreate("networkMissingAttrNomRue.xml", false);
        testInvalidCreate("networkMissingAttrVitesse.xml", false);
        testInvalidCreate("networkMissingAttrX.xml", false);
        testInvalidCreate("networkMissingAttrY.xml", false);
        testInvalidCreate("networkDuplicatedNoeud.xml", false);
        testInvalidCreate("networkDuplicatedTroncon.xml", false);
        testInvalidCreate("networkNegaviteAttrLongueur.xml", false);
        testInvalidCreate("networkNegaviteAttrVitesse.xml", false);
        testInvalidCreate("networkNullAttrVitesse.xml", false);
        testInvalidCreate("networkUnmatchingStreetName.xml", false);
        testInvalidCreate("networkWrongAttrDestination.xml", false);
        testInvalidCreate("networkWrongAttrLongueur.xml", false);
        testInvalidCreate("networkWrongAttrNodeId.xml", false);
        testInvalidCreate("networkWrongAttrVitesse.xml", false);
        testInvalidCreate("networkWrongAttrX.xml", false);
        testInvalidCreate("networkWrongAttrY.xml", false);
        testInvalidCreate("networkWrongNoeud.xml", false);
        testInvalidCreate("networkWrongTroncon.xml", true);
        testInvalidCreate("networkEmptyTroncon.xml", true);

        System.out.println("testInvalidCreateAll: OK");
    }

}
