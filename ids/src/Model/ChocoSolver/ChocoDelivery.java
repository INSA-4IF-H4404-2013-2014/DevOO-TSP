package Model.ChocoSolver;

import Model.City.Node;
import Model.Delivery.Delivery;
import Model.Delivery.Itinerary;
import cern.colt.list.IntArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nbuisson
 * Date: 09/12/13
 * Time: 09:25
 * To change this template use File | Settings | File Templates.
 */
public class ChocoDelivery {
    /** Choco ID used in ChocoGraph costs */
    private Integer chocoId;

    /** The delivery associatedd to the ChocoDelivery */
    private Delivery delivery;

    /** Address of the delivery */
    private Node address;

    /** The successors node of the delivery */
    private IntArrayList successorsNode;

    /** The list of itineraries linked to the delivery */
    private List<Itinerary> successorsItinerary;

    /**
     * Constructor
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
     * @param address the delivery node
     */
    public ChocoDelivery(Integer chocoId, Node address) {
        this.chocoId = chocoId;
        this.address = address;
        successorsItinerary = new ArrayList<Itinerary>();
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Node getAddress() {
        return address;
    }

    public void setSuccessorsNumber(int n) {
        successorsNode = new IntArrayList(n);
    }

    /**
     * Get the successors' list of the delivery
     * @return the list successors' ID of the delivery
     */
    public int[] getSuccessorsNode() {
        return successorsNode.elements();
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
     * @param nodeId the id of the node you want the linked itinerary
     * @return the itinerary
     */
    public Itinerary getItinerary(int nodeId) {
        int idx = successorsNode.indexOf(nodeId);
        return successorsItinerary.get(idx);
    }

    /**
     * Add a successor to the delivery
     * @param nodeId the ID of the successor node
     * @param itinerary the linked itinerary
     */
    public void addSuccessor(int nodeId, Itinerary itinerary) {
        successorsNode.add(nodeId);
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

    public int getSuccArcCost(int nodeId) {
        return getItinerary(nodeId).getCost();
    }

    public Integer getChocoId() {
        return chocoId;
    }
}
