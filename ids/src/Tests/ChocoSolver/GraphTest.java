package Tests.ChocoSolver;
import static org.junit.Assert.*;

import Model.ChocoSolver.ChocoGraph;
import Model.ChocoSolver.Graph;
import Model.City.Network;
import Model.Delivery.Round;
import Utils.UtilsException;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;


public class GraphTest {
	/**
	 * Checks that each arc of <code>RegularGraph(nbVertices,degree,minArcCost,maxArcCost)</code> 
	 * has a cost ranging between<code>minArcCost</code> and <code>maxArcCost</code>
	 */
	@Test
	public void testArcCost() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        Network network = Network.createFromXml("resources/tests/plan10x10.xml");
        Round round = Round.createFromXml("resources/tests/valid.xml", network);

        ChocoGraph graph = new ChocoGraph(network, round);
	}

}
