package Model.Delivery;

import Model.City.Network;
import Model.City.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:34
 * This class aims at managing a delivery defined by a delivery point (the client's address)
 * and a time frame
 */
public class Delivery {

    /** The delivery unique ID */
    private int id;

    /** The client who has to be delivered */
    private Client client;

    /** The delivery's address, an existing node in the graph */
    private Node address;

    /** The delivery's time frame */
    private Schedule schedule;

    /**
     * Constructor
     * @param id unique ID
     * @param client client who has to be delivered
     * @param address a node in the graph which is the delivery destination
     * @param schedule delivery's time frame
     */
    public Delivery(int id, Client client, Node address, Schedule schedule) {
        this.id = id;
        this.client = client;
        this.address = address;
        this.schedule = schedule;
        client.addDelivery(this);
    }

    /**
     * Constructor which parses XML nodes and attributes
     * @param round Round containing every schedules
     * @param schedule delivery's time frame
     * @param element Delivery XML element
     * @throws javax.xml.parsers.ParserConfigurationException If the parsing fails
     */
    public Delivery(Round round, Schedule schedule, Element element) throws ParserConfigurationException {
        String clientId;
        this.schedule = schedule;

        try {
            this.id = Integer.parseInt(element.getAttribute(XMLConstants.DELIVERY_DELIVERY_ID_ATTR));
        } catch(Exception e) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_DELIVERY_ID_ATTR +
                    "> de l'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> est invalide ou manquant (entier attendu).");
        }

        for(Delivery d : schedule.getDeliveries()) {
            if(d.getId() == this.id) {
                throw new ParserConfigurationException("La contrainte d'unicité sur l'attribut <" + XMLConstants.DELIVERY_DELIVERY_ID_ATTR +
                        "> de l'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> n'est pas respectée.");
            }
        }

        try {
            clientId = element.getAttribute(XMLConstants.DELIVERY_DELIVERY_CLIENT_ATTR);
        } catch(Exception e) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_DELIVERY_CLIENT_ATTR +
                    "> de l'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> est manquant.");
        }
        if(clientId.length() == 0) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_DELIVERY_CLIENT_ATTR +
                    "> de l'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> est vide.");
        }
        this.client = round.getClient(clientId);

        try {
            this.address = round.getNetwork().findNode(Integer.parseInt(element.getAttribute(XMLConstants.DELIVERY_DELIVERY_NODE_ATTR)));
        } catch(Exception e) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_DELIVERY_NODE_ATTR +
                    "> de l'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> est invalide ou manquant (entier attendu).");
        }
        if(this.address == null) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_DELIVERY_NODE_ATTR +
                    "> de l'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> ne référence pas un noeud existant (" +
                    Integer.parseInt(element.getAttribute(XMLConstants.DELIVERY_DELIVERY_NODE_ATTR)) +  ").");
        }

        client.addDelivery(this);
    }

    /**
     * Returns the delivery's ID
     * @return the delivery's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the delivery's address
     * @return the delivery's address
     */
    public Node getAddress() {
        return address;
    }

    /**
     * Returns the delivery's client
     * @return the delivery's client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Returns the delivery's schedule
     * @return the delivery's schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    public String toString() {
        return String.valueOf(id) + " - " + address.getId();
    }
}
