package Tests.ChocoSolver;

import Controller.MainWindowController;
import Model.ChocoSolver.ChocoGraph;
import Model.ChocoSolver.SolutionState;
import Model.ChocoSolver.TSP;
import Model.City.Network;
import Model.City.Node;
import Model.Delivery.Client;
import Model.Delivery.Round;
import Utils.UtilsException;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: nicolas
 * Date: 11/12/13
 * Time: 09:21
 * To change this template use File | Settings | File Templates.
 */
public class CalculatedRoundTest {
    /**
     * Checks that <code>tsp.getTotalCost()</code> is equal to the cost of the tour defined by <code>tsp.getPos()</code>
     */
    //@Test
    public void testCost() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        Network network = Network.createFromXml("resources/tests/chocograph/plan-test.xml");
        Round round = Round.createFromXml("resources/tests/chocograph/valid2.xml", network);

        ChocoGraph g = new ChocoGraph(network, round);

        int totalCost = 0;
        TSP tsp = new TSP(g);
        tsp.solve(200000,g.getNbVertices()*g.getMaxArcCost()+1);
        if (tsp.getSolutionState() != SolutionState.INCONSISTENT){
            int[] next = tsp.getNext();
            for (int i=0; i<g.getNbVertices(); i++)
                totalCost += g.getCost()[i][next[i]];
            assertEquals(totalCost,tsp.getTotalCost());
        }
        else
            assertTrue("No solution found after 200 seconds...", false);
    }

    @Test
    public void testEstimatedSchedule() {
        // TODO: test this function once computeRound() is ready

        Network myNetwork = new Network();

        myNetwork.createNode(1, 5, 5);
        myNetwork.createNode(2, 21, 10);
        myNetwork.createNode(3, 30, 60);

        myNetwork.createStreet("1");
        myNetwork.createStreet("2");
        myNetwork.createStreet("3");

        myNetwork.findStreet("1").createArc(myNetwork.findNode(1), myNetwork.findNode(2), 10, 100);
        myNetwork.findStreet("2").createArc(myNetwork.findNode(2), myNetwork.findNode(3), 10, 200);
        myNetwork.findStreet("3").createArc(myNetwork.findNode(3), myNetwork.findNode(1), 10, 100);

        Client client = new Client("Toto");

    }

    @Test
    public void testHtmlParser() {
        // TODO: testHtmlParser()
    }
}
