package Tests.ChocoSolver;

import Model.ChocoSolver.ChocoDelivery;
import Model.ChocoSolver.ChocoGraph;
import Model.City.Network;
import Model.City.Node;
import Model.Delivery.Round;
import Model.ChocoSolver.ChocoGraph;
import Utils.UtilsException;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Louise
 * Date: 11/12/13
 * Time: 09:07
 * To change this template use File | Settings | File Templates.
 */
public class ChocoGraphTest {
    /**
     * Tests the creation of a choco graph and checks that this one is correct
     * @throws UtilsException
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     */

    @Test
    public void testCreate3() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        Network network = Network.createFromXml("resources/tests/plan20x20.xml");
        Round round = Round.createFromXml("resources/tests/livraison20x20-2.xml", network);

        ChocoGraph chocograph = new ChocoGraph(network, round);
    }

    /**
     * Checks if choco graph is correct
     * @throws UtilsException
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     */
    @Test
    public void testCreate() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        Network network = Network.createFromXml("resources/tests/chocograph/plan-test.xml");
        Round round = Round.createFromXml("resources/tests/chocograph/valid2.xml", network);

        ChocoGraph chocograph = new ChocoGraph(network, round);

        int chocoId = chocograph.getChocoIdFromNetworkId(5);
        assertTrue(chocograph.getNbSucc(chocoId) == 2);
        int[] succ = chocograph.getSucc(chocoId);
        assertTrue(succ[0] == chocograph.getChocoIdFromNetworkId(7));
        assertTrue(succ[1] == chocograph.getChocoIdFromNetworkId(9));



        assertTrue(chocograph.getNbVertices() == 7);
        assertTrue(chocograph.getMinArcCost() == 100);
        assertTrue(chocograph.getMaxArcCost() == 900);

       int[][] testCost = chocograph.getCost();

        assertTrue(testCost[chocograph.getChocoIdFromNetworkId(3)][chocograph.getChocoIdFromNetworkId(7)] == 400);
        assertTrue(testCost[chocograph.getChocoIdFromNetworkId(5)][chocograph.getChocoIdFromNetworkId(9)] == 700);
        assertTrue(testCost[chocograph.getChocoIdFromNetworkId(3)][chocograph.getChocoIdFromNetworkId(5)] == 300);


        assertTrue(chocograph.getNbSucc(chocograph.getChocoIdFromNetworkId(0)) == 2);
        assertTrue(chocograph.getNbSucc(chocograph.getChocoIdFromNetworkId(1)) == 2);
        assertTrue(chocograph.getNbSucc(chocograph.getChocoIdFromNetworkId(3)) == 2);
        assertTrue(chocograph.getNbSucc(chocograph.getChocoIdFromNetworkId(9)) == 1);

        int[] testSucc = chocograph.getSucc(chocograph.getChocoIdFromNetworkId(3));
        assertTrue(testSucc[0] == chocograph.getChocoIdFromNetworkId(5) || testSucc[0] == chocograph.getChocoIdFromNetworkId(7));
        testSucc = chocograph.getSucc(chocograph.getChocoIdFromNetworkId(9));
        assertTrue(testSucc[0] == 0);
    }

    /**
     * Checks a bigger choco graph creation
     * @throws UtilsException
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     */
    @Test
    public void testCreate2() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        Network network = Network.createFromXml("resources/tests/plan20x20.xml");
        Round round = Round.createFromXml("resources/tests/livraison20x20-2.xml", network);

        ChocoGraph chocograph = new ChocoGraph(network, round);
        assertEquals(chocograph.getNbVertices(), 13);
    }
}
