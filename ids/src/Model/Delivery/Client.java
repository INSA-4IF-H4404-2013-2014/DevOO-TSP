package model.Delivery;

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

    /** A list of deliveries */
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
     * Removes a delivery to the client deliveries list
     * @param delivery the delivery to remove
     */
    public void removeDelivery(Delivery delivery) {
        deliveries.remove(delivery);
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

    /**
     * Represents a client in a string
     * @return The string
     */
    public String toString() {
        return id;
    }
}
