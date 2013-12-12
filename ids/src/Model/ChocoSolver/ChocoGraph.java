package Model.ChocoSolver;

import Model.City.Arc;
import Model.City.Network;
import Model.City.Node;
import Model.Delivery.Delivery;
import Model.Delivery.Itinerary;
import Model.Delivery.Round;
import Model.Delivery.Schedule;

import java.util.*;

/**
 * @author Rémi Domingues
 * This class aims at creating a graph defined as follow, from a network parsed from an XML file :
 * - Node : warehouse and delivery node from the XML network
 * - Arc : shortest path from a delivery/warehouse node to an other delivery/warehouse node
 */
public class ChocoGraph implements Graph {
    /**
     * Node information used by the Dijkstra algorithm
     * */
    class NodeInfo {
        /** true if the node has been visited by Dijkstra, false else */
        boolean visited = false;

        /** Smallest cost (time) currently found in order to go from this node to a source node (@see Dijkstra) */
        int cost = Integer.MAX_VALUE;

        /** Predecessor in the shortest path currently found in order to go to this source node */
        Integer previous = null;
    }

    /**
     * Key : real network node ID
     * Value : Choco Delivery => targeted delivery node + itinerary in order to go to successors delivery nodes
     */
    private Map<Integer, ChocoDelivery> deliveries = new HashMap<Integer, ChocoDelivery>();

    /**
     * Key : a Choco graph node id, between 0 and nbVertices
     * Value : a real network id, from the xml file
     * Note : 0 is the warehouse's ID
     */
    private Map<Integer, Integer> nodesId = new HashMap<Integer, Integer>();

    /** The number of verticies (warehouse included) of the choco graph */
	private int nbVertices;

    /** The maximal arc cost in this graph */
	private int maxArcCost;

    /** The minimal arc cost in this graph */
	private int minArcCost;

    /**
     * A cost matrix
     * cost[i][j] is the cost of the(i,j) arc (from i to j)
     * If an arc does not exist, the cost is maxArcCost + 1
     */
	private int[][] cost;

    /**
     * Constructor which initialized the choco graph and its costs
     * @param network The network containing the nodes
     * @param round The round containing the deliveries
     */
    public ChocoGraph(Network network, Round round) {
        nbVertices = round.getDeliveryList().size() + 1;
        this.maxArcCost = Integer.MIN_VALUE;
        this.minArcCost = Integer.MAX_VALUE;

        createGraph(network, round);
        initCosts();
    }

    /**
     * Calculates the costs of the choco graph
     */
    private void initCosts() {
        ChocoDelivery cd;
        int chocoId;

        cost = new int[nbVertices][];

        //Initializing cost matrix and calculating max arc cost
        //Unexisting arcs are initialized with -1
        for(int i = 0; i < nbVertices; ++i) {
            cost[i] = new int[nbVertices];
            cd = getChocoDeliveryFromChocoId(i);

            //Initializing successors cost for a delivery node at -1
            for(int j = 0; j < nbVertices; ++j) {
                cost[i][j] = -1;
            }

            //Initializing successors cost for a delivery node with real cost for existing arcs
            for(int realNodeId : cd.getSuccessorsNode()) {
                chocoId = getChocoDeliveryFromNetworkId(realNodeId).getChocoId();
                cost[i][chocoId] = cd.getSuccArcCost(realNodeId);

                if(cost[i][chocoId] > this.maxArcCost) {
                    this.maxArcCost = cost[i][chocoId];
                }

                if(cost[i][chocoId] < this.minArcCost) {
                    this.minArcCost = cost[i][chocoId];
                }
            }
        }

        //Initializing unexisting arcs with max cost + 1
        for(int i = 0; i < nbVertices; ++i) {
            for(int j = 0; j < nbVertices; ++j) {
                if(cost[i][j] == -1) {
                    cost[i][j] = maxArcCost + 1;
                }

            }
        }
    }

    /**
     * Calls Dijkstra in order to calculates the shortest itineraries between the warehouse/delivery nodes,
     * and creates a choco delivery for each of these nodes
     * @param network The network containing the nodes
     * @param round The round containing the deliveries
     */
    private void createGraph(Network network, Round round) {
        Map<Integer, NodeInfo> dict = new HashMap<Integer, NodeInfo>();
        Schedule firstSchedule, lastSchedule;

        Node warehouse = round.getWarehouse();
        List<Schedule> schedules = initChocoDeliveries(round);
        initDict(network, dict);

        firstSchedule = linkWarehouseToDeliveries(network, dict, schedules, warehouse);

        lastSchedule = linkDeliveries(network, dict, schedules, firstSchedule);

        linkDeliveriesToWarehouse(network, dict, lastSchedule, warehouse);
    }

    /**
     * Initialized the choco deliveries map attribute by adding the warehouse and delivery nodes
     * A schedules temporary list is also returned
     * @param round The round
     * @return A temporary schedule list which will contain every schedule
     */
    private List<Schedule> initChocoDeliveries(Round round) {
        int i = 0;
        List<Schedule> schedules = new LinkedList<Schedule>();

        ChocoDelivery warehouse = new ChocoDelivery(0, round.getWarehouse());
        deliveries.put(round.getWarehouse().getId(), warehouse);
        nodesId.put(i, round.getWarehouse().getId());
        ++i;

        for(Schedule s : round.getSchedules()) {
            if(!s.getDeliveries().isEmpty()) {
                for(Delivery d : s.getDeliveries()) {
                    deliveries.put(d.getAddress().getId(), new ChocoDelivery(i, d));
                    nodesId.put(i, d.getAddress().getId());
                    ++i;
                }
                schedules.add(s);
            }
        }

        return schedules;
    }

    //Linking warehouse with every node of the first distinct schedule
    private Schedule linkWarehouseToDeliveries(Network network, Map<Integer, NodeInfo> dict, List<Schedule> schedules, Node warehouse) {
        Schedule firstSchedule = null;

        if(!schedules.isEmpty()) {
            List<Node> successors = new LinkedList<Node>();

            firstSchedule = getNextSchedule(schedules);
            for(Delivery d : firstSchedule.getDeliveries()) {
                successors.add(d.getAddress());
            }

            computeDistinctScheduleArcs(network, warehouse, successors, dict);
        }

        return firstSchedule;
    }

    //For each distinct schedule ds
    //  Linking every node of ds to every node of ds
    //  AND
    //  Linking every node of ds to every node of the next distinct schedule

    private Schedule linkDeliveries(Network network, Map<Integer, NodeInfo> dict, List<Schedule> schedules, Schedule currentSchedule) {
        Schedule nextSchedule;

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

        return currentSchedule;
    }



    //Linking every node of the last distinct schedule to the warehouse
    private void linkDeliveriesToWarehouse(Network network, Map<Integer, NodeInfo> dict, Schedule lastSchedule, Node warehouse) {
        if(lastSchedule != null) {

            for(Delivery source : lastSchedule.getDeliveries()) {
                List<Node> successors = new LinkedList<Node>();
                successors.add(warehouse);
                for(Delivery d : lastSchedule.getDeliveries()) {
                    if(d != source) {
                        successors.add(d.getAddress());
                    }
                }

                reinitDict(dict);
                computeDistinctScheduleArcs(network, source.getAddress(), successors, dict);
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
        List<Node> succCopy = new LinkedList<Node>(successors);
        ChocoDelivery current = deliveries.get(source.getId());
        current.setSuccessorsNumber(successors.size());

        runDijkstra(network, source.getId(), succCopy, dict);

        for(Node n : successors) {
            shortestPath = getShortestPath(dict, n.getId());
            directions = getDirections(network, shortestPath);
            current.addSuccessor(n.getId(), deliveries.get(n.getId()).getChocoId(), new Itinerary(source, n, directions));
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
                        if(!tmpNodeInfo.visited && !neighbours.contains(tmpNodeId)) {
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
            path.add(0, tmp);
            tmp = tmpNodeInfo.previous;
            tmpNodeInfo = dict.get(tmp);
        }

        path.add(0, tmp);

        return path;
    }

    public ChocoDelivery getChocoDeliveryFromChocoId(Integer nodeId) {
        return deliveries.get(nodesId.get(nodeId));
    }

    public ChocoDelivery getChocoDeliveryFromNetworkId(Integer nodeId) {
        return deliveries.get(nodeId);
    }

    public Integer getChocoIdFromNetworkId(Integer nodeId) {
        return deliveries.get(nodeId).getChocoId();
    }

    public Integer getNetworkIdFromChocoId(Integer chocoId) {
        return nodesId.get(chocoId);
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
		return deliveries.get(nodesId.get(i)).getSuccessorsChocoNode();
	}


	public int getNbSucc(int i) {
        return deliveries.get(nodesId.get(i)).getSuccessorsChocoNode().length;
	}

    public Map<Integer, ChocoDelivery> getDeliveries() {
        return deliveries;
    }
}
