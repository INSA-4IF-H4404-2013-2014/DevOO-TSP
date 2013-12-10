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
    public ChocoDelivery(Delivery delivery) {
        this.delivery = delivery;
        this.address = delivery.getAddress();
        successorsNode = new IntArrayList();
        successorsItinerary = new ArrayList<Itinerary>();
    }

    /**
     * Constructor
     * @param address the delivery node
     */
    public ChocoDelivery(Node address) {
        this.address = address;
        successorsNode = new IntArrayList();
        successorsItinerary = new ArrayList<Itinerary>();
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Node getAddress() {
        return address;
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
        return successorsItinerary.get(nodeId);
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
        int[] costs = new int[successorsItinerary.size()];
        int i = -1;

        for(Itinerary itinerary:successorsItinerary) {
            i++;
            costs[i] = itinerary.getCost();
        }

        return costs;
    }
}
