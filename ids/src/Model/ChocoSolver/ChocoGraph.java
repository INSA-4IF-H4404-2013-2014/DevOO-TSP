package Model.ChocoSolver;

import Model.City.Network;
import Model.Delivery.Delivery;
import Model.Delivery.Round;
import Model.Delivery.Schedule;

import java.util.*;

public class ChocoGraph implements Graph {
    private Map<Integer, ChocoDelivery> deliveries;

	private int nbVertices;

	private int maxArcCost;

	private int minArcCost;

	private int[][] cost;

    /**
     * On doit créer un arc entre une livraison
         et toutes les livraisons de sa plage horaire et après

         Donc deux passes :
            on crée les chocodelivery
            pour chaque livraison
                déterminer les livraisons visées
                run dijkstra tant que on a pas atteint toutes les livraisons VISEES(int décrémenté)
                 ajouter à la livraison un itinéraire créé grâce à Dijkstra

     * @param network
     * @param round
     */
    public ChocoGraph(Network network, Round round) {
        class NodeInfo {

        }
        List<Delivery> succ;

        for(Schedule s : round.getSchedules()) {
            succ = getSucc(round, s);

            for(Delivery d : s.getDeliveries()) {
                //Adding the delivery to the ChocoDelivery map
                deliveries.put(d.getId(), new ChocoDelivery(d));

                runDijkstra(network, d, succ);
            }
        }
    }

    private List<Delivery> getSucc(Round round, Schedule schedule) {
        List<Delivery> succ = new LinkedList<Delivery>();

        for(Schedule s : round.getSchedules()) {
            if(!schedule.getEarliestBound().after(s.getLatestBound())) {
                for(Delivery d : s.getDeliveries()) {
                    succ.add(d);
                }
            }
        }

        return succ;
    }

    private void runDijkstra(Network network, Delivery delivery, List<Delivery> succ) {
        Map<Integer, Integer> dist;
        List<Integer> visited;
    }

    public Delivery getDelivery(Integer nodeId) {
        return deliveries.get(nodeId).getDelivery();
    }

	public int getMaxArcCost() {
		return maxArcCost;
	}

	public int getMinArcCost() {
		return minArcCost;
	}

	public int getNbVertices() {
		return nbVertices;
	}

	public int[][] getCost(){
		return cost;
	}

	public int[] getSucc(int i) {
		return deliveries.get(i).getSuccessorsNode();
	}


	public int getNbSucc(int i) {
        return deliveries.get(i).getSuccessorsNode().length;
	}

}
