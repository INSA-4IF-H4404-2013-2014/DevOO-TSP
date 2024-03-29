package Tests.ChocoSolver;
import static org.junit.Assert.*;

import Model.ChocoSolver.ChocoGraph;
import Model.ChocoSolver.Graph;
import Model.ChocoSolver.SolutionState;
import Model.ChocoSolver.TSP;
import Model.City.Network;
import Model.Delivery.Round;
import Utils.UtilsException;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;


public class TSPTest {
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

    /**
     * Checks that <code>tsp.getTotalCost()</code> is equal to the cost of the tour defined by <code>tsp.getPos()</code>
     */
    @Test
    public void testFindPath() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        Network network = Network.createFromXml("resources/tests/plan10x10.xml");
        Round round = Round.createFromXml("resources/tests/livraison10x10-4.xml", network);

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

	/**
	 * Checks (with a Branch and Bound algorithm) that <code>tsp.getTotalCost()</code> is the best solution
	 */
	//@Test
	public void testBestSol() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        Network network = Network.createFromXml("resources/tests/plan10x10.xml");
        Round round = Round.createFromXml("resources/tests/valid2.xml", network);

        ChocoGraph g = new ChocoGraph(network, round);
		TSP tsp = new TSP(g);
		long tps = System.currentTimeMillis();
		tsp.solve(200000,g.getNbVertices()*g.getMaxArcCost()+1);
		System.out.print("testBestSol: "+(System.currentTimeMillis()-tps)+" ms for Choco");
		if (tsp.getSolutionState() == SolutionState.OPTIMAL_SOLUTION_FOUND){
			tps = System.currentTimeMillis();
			int sol = branchAndBound(g);
			System.out.println(" and "+(System.currentTimeMillis()-tps)+" ms for Branch and Bound");
			assertEquals(tsp.getTotalCost(), sol);
		}
		else 
			assertTrue("testBestSol: Optimal solution not found within 200 seconds",false);
	}
	
	/**
	 * Iteratively searches for tours until finding the optimal tour and proving its optimality, 
	 * by increasing the CPU timeLimit after each call to <code>solve</code>
	 */
	//@Test
	public void testLargeGraph() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        Network network = Network.createFromXml("resources/tests/plan10x10.xml");
        Round round = Round.createFromXml("resources/tests/valid2.xml", network);

        ChocoGraph g = new ChocoGraph(network, round);
		TSP tsp = new TSP(g);
		int bound = g.getNbVertices()*g.getMaxArcCost() + 1;
		long tps = System.currentTimeMillis();
		System.out.println("testLargeGraph: Initial bound on the tour = "+bound);
		for (int t = 10; (tsp.getSolutionState() != SolutionState.OPTIMAL_SOLUTION_FOUND) && 
						 (tsp.getSolutionState() != SolutionState.INCONSISTENT); t*=2){
			System.out.println("--> Search of a tour strictly lower than "+bound+" within a time limit of "+t+"s.");
			tsp.solve(t*1000,bound-1);
			if (tsp.getSolutionState() == SolutionState.NO_SOLUTION_FOUND){
				System.out.println("    Time limit of "+t+"s reached before having completed the search.");
				System.out.println("    No solution strictly lower than "+bound+" has been found.");
			}
			else if (tsp.getSolutionState() == SolutionState.INCONSISTENT)
				System.out.println("There does not exist a solution strictly lower than "+bound);
			else if (tsp.getSolutionState() == SolutionState.OPTIMAL_SOLUTION_FOUND)
				System.out.println("    Optimal solution found: totalCost="+tsp.getTotalCost());
			else{ // etat = SOLUTION_FOUND
				System.out.println("    Time limit of "+t+"s reached before having completed the search.");
				System.out.println("    Best solution found so far: totalCost="+tsp.getTotalCost());
				bound = tsp.getTotalCost();
			}
		}	
		System.out.println("Total time for finding the optimum solution, and proving its optimality = "+(System.currentTimeMillis()-tps)/1000+"s");
	}
	
	/**
	 * Case of a graph such that minArcCost = maxArcCost
	 */
	//@Test
	public void testConstantCosts() throws UtilsException, ParserConfigurationException, FileNotFoundException {
        Network network = Network.createFromXml("resources/tests/plan10x10.xml");
        Round round = Round.createFromXml("resources/tests/valid2.xml", network);

        ChocoGraph g = new ChocoGraph(network, round);
		TSP tsp = new TSP(g);
		int bound = g.getNbVertices()*g.getMaxArcCost() + 1;
		tsp.solve(50000,bound);
        assertTrue(tsp.getSolutionState() != SolutionState.INCONSISTENT);
	}
	
	private int currentBestBound;
	
	private int branchAndBound(Graph g){
		int[] perm = new int[g.getNbVertices()];
		for (int i=0; i<g.getNbVertices(); i++)
			perm[i] = i;
		currentBestBound = g.getNbVertices()*g.getMaxArcCost();
		branchAndBound(1, 0, perm, g);
		return currentBestBound;
	}
	
	private void branchAndBound(int i, int currentCost, int[] perm, Graph g){
		if (i == g.getNbVertices()){
			currentCost += g.getCost()[perm[g.getNbVertices()-1]][0];
			if (currentCost < currentBestBound)
				currentBestBound = currentCost;
		}
		else if (currentCost+g.getMinArcCost()*(g.getNbVertices()-i) < currentBestBound){
			int ival = perm[i];
			for (int j=i; j<g.getNbVertices(); j++){
					int jval = perm[j];
					perm[j] = ival;
					perm[i] = jval;
					branchAndBound(i+1, currentCost + g.getCost()[perm[i-1]][jval], perm, g);
					perm[j] = jval;
					perm[i] = ival;
			}
		}
	}
}


