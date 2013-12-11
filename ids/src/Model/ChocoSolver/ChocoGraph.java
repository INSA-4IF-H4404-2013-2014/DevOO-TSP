package Model.ChocoSolver;

import Model.City.Arc;
import Model.City.Network;
import Model.City.Node;
import Model.Delivery.Delivery;
import Model.Delivery.Itinerary;
import Model.Delivery.Round;
import Model.Delivery.Schedule;

import java.util.*;

public class ChocoGraph implements Graph {
    class NodeInfo {
        int cost = Integer.MAX_VALUE;
        boolean visited = false;
        Integer previous = null;
    }

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
        List<Schedule> schedules = new LinkedList<Schedule>();
        Schedule currentSchedule = null, nextSchedule;

        //Initializing schedules temporary list and the delivery map
        ChocoDelivery warehouse = new ChocoDelivery(round.getWarehouse());
        deliveries.put(round.getWarehouse().getId(), warehouse);

        for(Schedule s : round.getSchedules()) {
            if(!s.getDeliveries().isEmpty()) {
                for(Delivery d : s.getDeliveries()) {
                    deliveries.put(d.getId(), new ChocoDelivery(d));
                }
                schedules.add(s);
            }
        }

        //Linking warehouse with every node of the first distinct schedule
        if(!schedules.isEmpty()) {
            List<Delivery> successors = new LinkedList<Delivery>();

            currentSchedule = getNextSchedule(schedules);
            successors.addAll(currentSchedule.getDeliveries());

            computeDistinctScheduleArcs(network, warehouse.getDelivery(), successors);
        }

        //For each distinct schedule ds
        //  Linking every node of ds to every node of ds
        //  AND
        //  Linking every node of ds to every node of the next distinct schedule
        while(!schedules.isEmpty()) {
            nextSchedule = getNextSchedule(schedules);

            for(Delivery source : currentSchedule.getDeliveries())
            {
                List<Delivery> successors = new LinkedList<Delivery>();

                for(Delivery d : currentSchedule.getDeliveries()) {
                    if(d != source) {
                        successors.add(d);
                    }
                }
                successors.addAll(nextSchedule.getDeliveries());

                computeDistinctScheduleArcs(network, source, successors);
            }

            currentSchedule = nextSchedule;
        }

        //Linking every node of the last distinct schedule to the warehouse
        if(currentSchedule != null) {
            List<Delivery> successors = new LinkedList<Delivery>();
            successors.add(warehouse.getDelivery());

            for(Delivery d : currentSchedule.getDeliveries()) {
                computeDistinctScheduleArcs(network, d, successors);
            }
        }
    }

    private Schedule getNextSchedule(List<Schedule> schedules) {
        Schedule minSchedule = null, tmp;
        ListIterator<Schedule> minIter = null, iter = schedules.listIterator();

        while(iter.hasNext()) {
            tmp = iter.next();
            if(tmp.getEarliestBound().before(minSchedule.getEarliestBound())) {
                minSchedule = tmp;
                minIter = iter;
            }
        }

        if(minIter != null) {
            minIter.remove();
        }

        return minSchedule;
    }

    private void computeDistinctScheduleArcs(Network network, Delivery source, List<Delivery> successors) {
        Map<Integer, NodeInfo> dict;
        List<Integer> shortestPath;
        List<Arc> directions;

        dict = runDijkstra(network, source.getId(), successors);

        for(Delivery d : successors) {
            shortestPath = getShortestPath(dict, d.getId());
            directions = getDirections(network, shortestPath);
            deliveries.get(source.getId()).addSuccessor(d.getId(), new Itinerary(source.getAddress(), d.getAddress(), directions));
        }
    }

    private List<Arc> getDirections(Network network, List<Integer> nodesList) {
        List<Arc> directions = new LinkedList<Arc>();
        ListIterator<Integer> iter = nodesList.listIterator();
        Node currentNode, nextNode;

        if(iter.hasNext()) {
            currentNode = network.findNode(iter.next());

            while(iter.hasNext()) {
                nextNode = network.findNode(iter.next());

                directions.add(currentNode.findOutgoingTo(nextNode));

                currentNode = nextNode;
            }
        }

        return directions;
    }



    private Integer getMinUnvisited(Map<Integer, NodeInfo> dict, List<Integer> neighbours) {
        int min = Integer.MAX_VALUE;
        NodeInfo n, minNodeInfo = null;
        boolean found = false;
        ListIterator<Integer> minIter = null, iter;
        Integer tmp, selected = null;

        iter = neighbours.listIterator();
        while(iter.hasNext() && !found) {
            tmp = iter.next();
            n = dict.get(tmp);
            if(n.cost < min && n.visited == false) {
                min = n.cost;
                minNodeInfo = n;
                minIter = iter;
                selected = tmp;
                found = true;
            }
        }

        minIter.remove();
        minNodeInfo.visited = true;

        return selected;
    }

    private Map<Integer, NodeInfo> runDijkstra(Network network, Integer source, List<Delivery> succ) {
        //Variable declaration and initialization
        NodeInfo tmpNodeInfo;
        int tmpDist;
        boolean found;
        Delivery tmpDelivery;
        ListIterator<Delivery> iter;
        Integer current, tmpNodeId;
        List<Integer> neighbours = new LinkedList<Integer>();
        Map<Integer, NodeInfo> dict = new HashMap<Integer, NodeInfo>();

        //Initializing search from source
        dict.get(source).cost = 0;
        neighbours.add(source);

        //While every targeted delivery node has not been reached
        while(!neighbours.isEmpty() && !succ.isEmpty()) {
            current = getMinUnvisited(dict, neighbours);

            //We stop the search if we have found every delivery node
            iter = succ.listIterator();
            found = false;
            while(iter.hasNext() && !found) {
                tmpDelivery = iter.next();
                if(tmpDelivery.getAddress().getId() == current) {
                    iter.remove();
                    found = true;
                }
            }

            if(!succ.isEmpty()) {
                for(Arc a : network.findNode(current).getOutgoing()) {
                    tmpNodeId = a.getTo().getId();
                    tmpDist = dict.get(current).cost + network.getCost(current, tmpNodeId);
                    tmpNodeInfo = dict.get(tmpNodeId);
                    if(tmpDist < tmpNodeInfo.cost) {
                        tmpNodeInfo.cost = tmpDist;
                        tmpNodeInfo.previous = current;
                        if(!tmpNodeInfo.visited) {
                            neighbours.add(tmpNodeId);
                        }
                    }
                }
            }
        }

        return dict;
    }

    private List<Integer> getShortestPath(Map<Integer, NodeInfo> dict, Integer target) {
        Integer tmp = target;
        NodeInfo tmpNodeInfo = dict.get(tmp);
        List<Integer> path = new LinkedList<Integer>();

        while(tmpNodeInfo.previous != null) {
            path.add(tmp, 0);
            tmp = tmpNodeInfo.previous;
            tmpNodeInfo = dict.get(tmp);
        }

        return path;
    }

    public ChocoDelivery getDelivery(Integer nodeId) {
        return deliveries.get(nodeId);
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
