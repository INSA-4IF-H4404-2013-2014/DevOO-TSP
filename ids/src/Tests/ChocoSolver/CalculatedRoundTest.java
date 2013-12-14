package Tests.ChocoSolver;

import Controller.MainWindowController;
import Model.ChocoSolver.CalculatedRound;
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
import java.util.Calendar;

import static org.junit.Assert.*;

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
    public void testEstimatedSchedule() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        MainWindowController main = new MainWindowController();

        main.loadNetwork("resources/tests/planTiny.xml");
        main.loadRound("resources/tests/livraisonTiny.xml");

        CalculatedRound calculatedRound = main.getMainWindow().getCalculatedRound();

        assert(calculatedRound.getEstimatedSchedules(7).get(Calendar.HOUR_OF_DAY) == 8);
        assert(calculatedRound.getEstimatedSchedules(7).get(Calendar.MINUTE) == 00);

        assert(calculatedRound.getEstimatedSchedules(5).get(Calendar.HOUR_OF_DAY) == 9);
        assert(calculatedRound.getEstimatedSchedules(5).get(Calendar.MINUTE) == 30);

        assert(calculatedRound.getEstimatedSchedules(9).get(Calendar.HOUR_OF_DAY) == 9);
        assert(calculatedRound.getEstimatedSchedules(9).get(Calendar.MINUTE) == 42);
    }

    /*@Test
    public void testHtmlParser() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        MainWindowController main = new MainWindowController();

        main.loadNetwork("resources/tests/planTiny.xml");
        main.loadRound("resources/tests/livraisonTiny.xml");
        System.out.println(main.getMainWindow().getCalculatedRound().calculatedRoundToHtml());
    }*/
}
