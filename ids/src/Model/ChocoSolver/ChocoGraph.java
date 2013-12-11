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
        boolean visited = false;
        int cost = Integer.MAX_VALUE;
        Integer previous = null;
    }

    /** HashMap which has as Key : node ID and Value : Choco Delivery => targeted node + itinerary */
    private Map<Integer, ChocoDelivery> deliveries = new HashMap<Integer, ChocoDelivery>();

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
        Map<Integer, NodeInfo> dict = new HashMap<Integer, NodeInfo>();
        List<Schedule> schedules = new LinkedList<Schedule>();
        Schedule currentSchedule = null, nextSchedule;

        //Initializing schedules temporary list and the delivery map
        ChocoDelivery warehouse = new ChocoDelivery(round.getWarehouse());
        deliveries.put(round.getWarehouse().getId(), warehouse);

        for(Schedule s : round.getSchedules()) {
            if(!s.getDeliveries().isEmpty()) {
                for(Delivery d : s.getDeliveries()) {
                    deliveries.put(d.getAddress().getId(), new ChocoDelivery(d));
                }
                schedules.add(s);
            }
        }

        //Linking warehouse with every node of the first distinct schedule
        if(!schedules.isEmpty()) {
            List<Node> successors = new LinkedList<Node>();

            currentSchedule = getNextSchedule(schedules);
            for(Delivery d : currentSchedule.getDeliveries()) {
                successors.add(d.getAddress());
            }

            initDict(network, dict);
            computeDistinctScheduleArcs(network, warehouse.getAddress(), successors, dict);
        }

        //For each distinct schedule ds
        //  Linking every node of ds to every node of ds
        //  AND
        //  Linking every node of ds to every node of the next distinct schedule
        while(!schedules.isEmpty()) {
            nextSchedule = getNextSchedule(schedules);

            for(Delivery source : currentSchedule.getDeliveries())
            {
                List<Node> successors = new LinkedList<Node>();

                for(Delivery d : currentSchedule.getDeliveries()) {
                    if(d != source) {
                        successors.add(d.getAddress());
                    }
                }
                for(Delivery d : nextSchedule.getDeliveries()) {
                    successors.add(d.getAddress());
                }

                reinitDict(dict);
                computeDistinctScheduleArcs(network, source.getAddress(), successors, dict);
            }

            currentSchedule = nextSchedule;
        }

        //Linking every node of the last distinct schedule to the warehouse
        if(currentSchedule != null) {
            List<Node> successors = new LinkedList<Node>();

            for(Delivery d : currentSchedule.getDeliveries()) {
                reinitDict(dict);
                successors.add(warehouse.getAddress());
                computeDistinctScheduleArcs(network, d.getAddress(), successors, dict);
            }
        }
    }

    private void initDict(Network network, Map<Integer, NodeInfo> dict) {
        for(Integer i : network.getNodes().keySet()) {
            dict.put(i, new NodeInfo());
        }
    }

    private void reinitDict(Map<Integer, NodeInfo> dict) {
        for(NodeInfo value : dict.values()) {
            value.cost = Integer.MAX_VALUE;
            value.visited = false;
            value.previous = null;
        }
    }

    private Schedule getNextSchedule(List<Schedule> schedules) {
        Schedule minSchedule = null, tmp;
        ListIterator<Schedule> iter = schedules.listIterator();

        if(iter.hasNext()) {
            minSchedule = iter.next();

            while(iter.hasNext()) {
                tmp = iter.next();
                if(tmp.getEarliestBound().before(minSchedule.getEarliestBound())) {
                    minSchedule = tmp;
                }
            }

            schedules.remove(minSchedule);
        }


        return minSchedule;
    }

    //Find shortest past (all nodes and arcs) between source and each next delivery for a network
    private void computeDistinctScheduleArcs(Network network, Node source, List<Node> successors, Map<Integer, NodeInfo> dict) {
        List<Integer> shortestPath;
        List<Arc> directions;

        runDijkstra(network, source.getId(), successors, dict);

        for(Node n : successors) {
            shortestPath = getShortestPath(dict, n.getId());
            directions = getDirections(network, shortestPath);
            deliveries.get(source.getId()).addSuccessor(n.getId(), new Itinerary(source, n, directions));
        }
    }

    //In a network, find arc between two successive nodes taken in a organized list of nodes
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

    //Find next node with minimal cost and not visited yet
    private Integer getMinUnvisited(Map<Integer, NodeInfo> dict, List<Integer> neighbours) {
        int min = Integer.MAX_VALUE;
        NodeInfo n, minNodeInfo = null;
        boolean found = false;
        Integer tmp, selected = null;
        ListIterator<Integer> iter = neighbours.listIterator();

        while(iter.hasNext()) {
            tmp = iter.next();
            n = dict.get(tmp);
            if(n.cost < min && !n.visited) {
                min = n.cost;
                minNodeInfo = n;
                selected = tmp;
            }
        }

        neighbours.remove(selected);
        minNodeInfo.visited = true;

        return selected;
    }

    //Yeah, that's Dijkstra. Trivial.
    private Map<Integer, NodeInfo> runDijkstra(Network network, Integer source, List<Node> succ, Map<Integer, NodeInfo> dict) {
        //Variable declaration and initialization
        NodeInfo tmpNodeInfo;
        int tmpDist;
        boolean found;
        ListIterator<Node> iter;
        Node tmpNode;
        Integer current, tmpNodeId;
        List<Integer> neighbours = new LinkedList<Integer>();

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
                tmpNode = iter.next();
                if(tmpNode.getId() == current) {
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

    //Give the shortest path make of nodes between source and target based on Dijkstra graph
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
