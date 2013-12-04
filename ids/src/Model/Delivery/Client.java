package Model.Delivery;

import Model.City.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:35
 * This class aims at managing a client's information
 */
public class Client {
    /** The client's unique ID */
    private String id;

    /** An ordered list of deliveries which defines the round */
    private List<Delivery> deliveries = new LinkedList<Delivery>();

    /**
     * Constructor
     * @param id The client's unique ID
     */
    public Client(String id) {
        this.id = id;
    }

    /**
     * Adds a delivery to the client deliveries list
     * @param delivery the delivery to add
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    /**
     * Returns the client's ID
     * @return the client's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the client's deliveries
     * @return the client's deliveries
     */
    public List<Delivery> getDeliveries() {
        return deliveries;
    }
}
