package Model.Delivery;

import Model.City.Node;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:35
 * This class aims at managing a client's information
 */
public class Client {
    /** The client's unique ID */
    private int id;

    /** The client's address, and existing node in the graph */
    private Node address;

    /**
     * Constructor
     * @param id The client's unique ID
     * @param address The client's address, and existing node in the graph
     */
    public Client(int id, Node address) {
        this.id = id;
        this.address = address;
    }

    /**
     * Returns the client's ID
     * @return the client's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the client's address
     * @return the client's address
     */
    public Node getAddress() {
        return address;
    }
}
