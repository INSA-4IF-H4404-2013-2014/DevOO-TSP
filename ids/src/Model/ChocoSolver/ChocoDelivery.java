package model.chocoSolver;

import model.city.Node;
import model.delivery.Delivery;
import model.delivery.Itinerary;
import cern.colt.list.IntArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rdomingues
 * Date: 09/12/13
 * Time: 09:25
 * This class aims at managing a choco node, which is a warehouse or a delivery
 */
public class ChocoDelivery {
    /** Choco ID used in ChocoGraph costs */
    private Integer chocoId;

    /** The delivery associatedd to the ChocoDelivery */
    private Delivery delivery;

    /** Address of the delivery */
    private Node address;

    /** The successors node of the delivery */
    private IntArrayList successorsNode = new IntArrayList(0);

    /** The successors node of the delivery identified by their choco ID */
    private IntArrayList successorsChocoNode = new IntArrayList(0);

    /** The list of itineraries linked to the delivery */
    private List<Itinerary> successorsItinerary;

    /**
     * Constructor
     * @param chocoId the choco node ID of this delivery
     * @param delivery the delivery
     */
    public ChocoDelivery(Integer chocoId, Delivery delivery) {
        this.chocoId = chocoId;
        this.delivery = delivery;
        this.address = delivery.getAddress();
        successorsItinerary = new ArrayList<Itinerary>();
    }

    /**
     * Constructor
     * @param chocoId the choco node ID of this delivery
     * @param address the delivery node
     */
    public ChocoDelivery(Integer chocoId, Node address) {
        this.chocoId = chocoId;
        this.address = address;
        successorsItinerary = new ArrayList<Itinerary>();
    }

    /**
     * Returns the delivery
     * @return @see description
     */
    public Delivery getDelivery() {
        return delivery;
    }

    /**
     * Returns the node of this delivery
     * @return @see description
     */
    public Node getAddress() {
        return address;
    }

    /**
     * Set the number of successors and choco successors of delivery
     */
    public void setSuccessorsNumber(int n) {
        successorsNode = new IntArrayList(n);
        successorsChocoNode = new IntArrayList(n);
    }

    /**
     * Get the successors' list of the delivery
     * @return the list of successors' ID of the delivery
     */
    public int[] getSuccessorsNode() {
        return successorsNode.elements();
    }

    /**
     * Get the choco successors' list of the delivery
     * @return the list of choco successors' ID of the delivery
     */
    public int[] getSuccessorsChocoNode() {
        return successorsChocoNode.elements();
    }

    /**
     * Get the list of itineraries linked to the delivery
     * @return the list of itineraries linked to the delivery
     */
    public List<Itinerary> getSuccessorsItinerary() {
        return successorsItinerary;
    }

    /**
     * Get the itinerary linked to a node
     * @param nodeId the network id of the node you want the linked itinerary
     * @return the itinerary
     */
    public Itinerary getItinerary(int nodeId) {
        int idx = successorsNode.indexOf(nodeId);
        return successorsItinerary.get(idx);
    }

    /**
     * Add a successor to the delivery
     * @param nodeId the network ID of the successor node
     * @param chocoId the choco ID of the successor node
     * @param itinerary the linked itinerary
     */
    public void addSuccessor(int nodeId, int chocoId, Itinerary itinerary) {
        successorsNode.add(nodeId);
        successorsChocoNode.add(chocoId);
        successorsItinerary.add(itinerary);
    }

    /**
     * Get the costs of each itineraries linked to the delivery
     * @return the costs of each itineraries linked to the delivery
     */
    public int[] getCosts() {
        int i = 0;
        int[] costs = new int[successorsItinerary.size()];

        for(Itinerary itinerary:successorsItinerary) {
            costs[i] = itinerary.getCost();
            ++i;
        }

        return costs;
    }

    /**
     * Returns the cost (time, unit according to the round xml file)
     * of the itinerary from a node to its successor
     * @param nodeId the source node
     * @return @see description
     */
    public int getSuccArcCost(int nodeId) {
        return getItinerary(nodeId).getCost();
    }

    /**
     * Returns the choco ID
     * @return @see description
     */
    public Integer getChocoId() {
        return chocoId;
    }
}
