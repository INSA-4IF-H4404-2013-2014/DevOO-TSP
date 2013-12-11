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

    @Test
    public void testCreate2() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        Network network = Network.createFromXml("resources/tests/chocograph/plan10x10.xml");
        Round round = Round.createFromXml("resources/tests/chocograph/livraison10x10-1.xml", network);

        ChocoGraph chocograph = new ChocoGraph(network, round);
    }

    @Test
    public void testCreate() throws UtilsException, FileNotFoundException, ParserConfigurationException {
        Network network = Network.createFromXml("resources/tests/chocograph/plan-test.xml");
        Round round = Round.createFromXml("resources/tests/chocograph/valid2.xml", network);

        ChocoGraph chocograph = new ChocoGraph(network, round);

        assertTrue(chocograph.getNbVertices() == 7);
        assertTrue(chocograph.getMinArcCost() == 1);
        assertTrue(chocograph.getMaxArcCost() == 9);

        int[][] testCost = chocograph.getCost();
        assertTrue(testCost[3][7] == 4);
        assertTrue(testCost[5][9] == 9);
        assertTrue(testCost[3][5] == 3);

        assertTrue(chocograph.getNbSucc(0) == 2);
        assertTrue(chocograph.getNbSucc(1) == 2);
        assertTrue(chocograph.getNbSucc(3) == 2);
        assertTrue(chocograph.getNbSucc(9) == 1);

        int[] testSucc = chocograph.getSucc(3);
        assertTrue(testSucc[0] == 5 || testSucc[0] == 7);
        testSucc = chocograph.getSucc(9);
        assertTrue(testSucc[0] == 0);
    }
}
