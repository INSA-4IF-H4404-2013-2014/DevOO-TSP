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

        /** Smallest cost (time) currently found in order to go from this node to a source node (@See Dijkstra) */
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
        Schedule firstSchedule, lastSchedule;

        Node warehouse = round.getWarehouse();
        List<Schedule> schedules = initChocoDeliveries(round);
        Map<Integer, NodeInfo> dict = initDict(network);

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

    /**
     * Initializes the warehouse's successors (every delivery node of the first schedule)
     * and its itineraries (choco arcs) using Dijkstra
     * @param network The initial network
     * @param dict Node information dictionary used by Dijkstra
     * @param schedules A temporary schedules list
     * @param warehouse The warehouse
     * @return The first schedule of the round
     */
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

    /**
     * For each delivery node of each schedule of the round, its successors (every delivery node of the same and next schedule)
     * and its itineraries (choco arcs) are initialized using Dijkstra
     * @param network The initial network
     * @param dict Node information dictionary used by Dijkstra
     * @param schedules A temporary schedules list
     * @param currentSchedule The first schedule of the round
     * @return The last schedule of the round
     */

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

    /**
     * For each delivery node of the last schedule of the round, its successors (every delivery node of the same schedule and the warehouse)
     * and its itineraries (choco arcs) are initialized using Dijkstra
     * @param network The initial network
     * @param dict Node information dictionary used by Dijkstra
     * @param lastSchedule The last schedule of the round
     * @param warehouse The warehouse
     */
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

    /**
     * Returns the Dijkstra dictionary with default values for ever delivery node and the warehouse
     * @param network The network
     * @return @See description
     */
    private Map<Integer, NodeInfo> initDict(Network network) {
        Map<Integer, NodeInfo> dict = new HashMap<Integer, NodeInfo>();

        for(Integer i : network.getNodes().keySet()) {
            dict.put(i, new NodeInfo());
        }

        return dict;
    }


    /**
     * Reinitialised the Dijkstra dictionary with default values for ever delivery node and the warehouse
     * @param dict The Dijkstra dictionary
     */
    private void reinitDict(Map<Integer, NodeInfo> dict) {
        for(NodeInfo value : dict.values()) {
            value.cost = Integer.MAX_VALUE;
            value.visited = false;
            value.previous = null;
        }
    }

    /**
     * Find the next schedule of the round according to its bounds, remove it from the schedules list and returns it
     * @param schedules A temporary schedules list, containing the next schedule we want to find
     * @return The earliest schedule of the schedules list given in parameter
     */
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

    /**
     * Calls a Dijkstra algorithm and initializes the shortest itineraries of the source for each of its successors
     * @param network The network
     * @param source The source node. Its successors and itineraries will be initialized
     * @param successors The successors nodes of the source
     * @param dict Dijkstra info structure
     */
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

    /**
     * Converts a list of successors nodes in a list of successors arcs from a network given
     * @param network The network containing the arcs and nodes
     * @param nodesList The successors nodes list (nodes path)
     * @return The successors arcs list (itinerary)
     */
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

    /**
     * Returns the node ID in the neighbours list which has the smallest cost in dict
     * i.e. : the node returned is the next one to take in the building a shortest path
     *        from a source (to the returned node id) (See Dijkstra)
     * Note : the neighbours list only contains unvisited nodes
     * @param dict Dijkstra info structure
     * @param neighbours A list of unvisited neighbours node (unvisited neighbours of every visited node in the network)
     * @return The closest unvisited node id in the neighbours list
     */
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

    /**
     * Dijkstra algorithm
     * This function initializes a dictionary structure which allows finding the shortest path from the source node to
     * any node of the network
     * Note : for calculation time reasons, this algorithm is stopped as soon as we have found the shortest path of every
     * node in the succ list
     * @param network The network containing every node and arc
     * @param source The source node. Every shortest path will go from this node to any node
     * @param succ This list must contains warehouse/delivery node IDs. The search will be stopped when a shortest path
     *             from the source to every successors has been found
     * @param dict Dijkstra info structure which will permit to calculates the shortest path from the source to any node
     */
    private void runDijkstra(Network network, Integer source, List<Node> succ, Map<Integer, NodeInfo> dict) {
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
    }

    //Give the shortest path make of nodes between source and target based on Dijkstra graph

    /**
     * Returns the shortest path from the source (the only node in dict which has its previous attribute as null => See Dijkstra)
     * to a target node of a network
     * @param dict Dijkstra info structure initialized
     * @param target A target node
     * @return The shortest path from the source to the target
     */
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

    /**
     * Returns the choco delivery from a choco node id
     * @param nodeId The choco node id
     * @return The choco delivery
     */
    public ChocoDelivery getChocoDeliveryFromChocoId(Integer nodeId) {
        return deliveries.get(nodesId.get(nodeId));
    }

    /**
     * Returns the choco delivery from a network node id
     * @param nodeId The network node id
     * @return The choco delivery
     */
    public ChocoDelivery getChocoDeliveryFromNetworkId(Integer nodeId) {
        return deliveries.get(nodeId);
    }

    /**
     * Returns the choco id from the network id
     * @param nodeId The network ID
     * @return The corresponding choco id
     */
    public Integer getChocoIdFromNetworkId(Integer nodeId) {
        return deliveries.get(nodeId).getChocoId();
    }

    /**
     * Returns the network id from the choco id
     * @param chocoId The choco ID
     * @return The corresponding network id
     */
    public Integer getNetworkIdFromChocoId(Integer chocoId) {
        return nodesId.get(chocoId);
    }

    /**
     * Returns the maximal arc cost of the choco graph
     * @return @See description
     */
    public int getMaxArcCost() {
		return maxArcCost;
	}

    /**
     * Returns the minimal arc cost of the choco graph
     * @return @See description
     */
	public int getMinArcCost() {
		return minArcCost;
	}

    /**
     * Returns the number of verticies of the choco graph,
     * i.e. the number of deliveries + 1 (warehouse)
     * @return @See description
     */
	public int getNbVertices() {
		return nbVertices;
	}

    /**
     * Returns the cost matrix of the graph
     * cost[i][j] is the cost of the(i,j) arc (from i to j)
     * If an arc does not exist, the cost is maxArcCost + 1
     * @return @See description
     */
	public int[][] getCost(){
		return cost;
	}

    /**
     * Returns the number of choco successors of a choco node
     * @param i a vertex such that 0 <= i < this.getNbVertices()
     * @return @See description
     */
	public int[] getSucc(int i) {
		return deliveries.get(nodesId.get(i)).getSuccessorsChocoNode();
	}

    /**
     * Returns the id of the choco successors nodes of a choco node
     * @param i a vertex such that 0 <= i < this.getNbVertices()
     * @return @See description
     */
	public int getNbSucc(int i) {
        return deliveries.get(nodesId.get(i)).getSuccessorsChocoNode().length;
	}

    /**
     * Returns a choco deliveries dictionary
     * Key : real network node ID
     * Value : Choco Delivery => targeted delivery node + itinerary in order to go to successors delivery nodes
     * @return @See description
     */
    public Map<Integer, ChocoDelivery> getDeliveries() {
        return deliveries;
    }
}
