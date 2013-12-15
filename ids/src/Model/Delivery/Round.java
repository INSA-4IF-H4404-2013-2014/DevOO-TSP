package Model.Delivery;

import Model.City.Network;
import Model.City.Node;
import Utils.UtilsException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:34
 * This Singleton aims at managing a round defined by a list of schdules containing deliveries
 */
public class Round {
    /** The network which contains the nodes */
    private Network network;

    /** The warehouse which is the start and end point of the round */
    private Node warehouse;

    /** The schedules containing the deliveries */
    private List<Schedule> schedules = new LinkedList<Schedule>();

    /** The clients delivered */
    private List<Client> clients = new LinkedList<Client>();

    /**
     * Constructor
     * @param network Network containing every node
     * @param warehouse start and end point of the round
     * @param schedules list of schedules containing the deliveries
     */
    private Round(Network network, Node warehouse, List<Schedule> schedules) {
        this.network = network;
        this.warehouse = warehouse;
        this.schedules = schedules;
    }

    /**
     * Constructor which parses XML nodes and attributes
     * @param network Network containing every node
     * @param root XML root element from an xml delivery file
     */
    private Round(Network network, Element root) throws ParserConfigurationException {
        this.network = network;

        NodeList xmlNodeList = root.getElementsByTagName(XMLConstants.DELIVERY_WAREHOUSE_ELEMENT);
        if(xmlNodeList == null || xmlNodeList.getLength() == 0) {
            throw new ParserConfigurationException("L'élément <" + XMLConstants.DELIVERY_WAREHOUSE_ELEMENT + "> est manquant");
        }

        try {
            this.warehouse = network.findNode(Integer.parseInt(((Element)xmlNodeList.item(0)).getAttribute(XMLConstants.DELIVERY_WAREHOUSE_NODE_ATTR)));
        } catch(Exception e) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_WAREHOUSE_NODE_ATTR + "> de l'élément <" + XMLConstants.DELIVERY_WAREHOUSE_ELEMENT + "> est invalide ou manquant (entier attendu).");
        }
        if(this.warehouse == null) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_WAREHOUSE_NODE_ATTR +
                    "> de l'élément <" + XMLConstants.DELIVERY_WAREHOUSE_ELEMENT + "> ne référence pas un noeud existant.");
        }

        xmlNodeList = root.getElementsByTagName(XMLConstants.DELIVERY_SCHEDULES_ELEMENT);
        if(xmlNodeList == null || xmlNodeList.getLength() == 0) {
            throw new ParserConfigurationException("L'élément <" + XMLConstants.DELIVERY_SCHEDULES_ELEMENT + "> est manquant.");
        }

        Element eSchedules = (Element) xmlNodeList.item(0);
        xmlNodeList = eSchedules.getElementsByTagName(XMLConstants.DELIVERY_SCHEDULE_ELEMENT);
        if(xmlNodeList == null || xmlNodeList.getLength() < 1) {
            throw new ParserConfigurationException("L'élément <" + XMLConstants.DELIVERY_SCHEDULES_ELEMENT + "> ne contient aucune plage horaire.");
        }

        for (int i = 0; i < xmlNodeList.getLength(); ++i) {
            Schedule s = new Schedule(this, (Element) xmlNodeList.item(i));
            schedules.add(s);

            for(Delivery d : s.getDeliveries())
                if(isTwiceDelivered(d.getAddress())) {
                    throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_DELIVERY_NODE_ATTR +
                            "> de l'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> référence un noeud déjà desservi (" +
                            d.getAddress().getId() +  ").");
            }
        }
    }

    /**
     * Add a delivery to the round
     * @param clientId The client ID
     * @param nodeId The targeted node to deliver
     * @param earliestBound The earliest bound, the delivery cannot be delivered before
     * @param latestBound The latest bound, the delivery cannot be delivered after
     */
    public void addDelivery(String clientId, int nodeId, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        Node node = network.findNode(nodeId);

        Client client = getClient(clientId);
        Schedule schedule = getSchedule(earliestBound, latestBound);

        Delivery delivery = new Delivery(schedule.getNextDeliveryId(), client, node, schedule);

        client.addDelivery(delivery);
        schedule.addDelivery(delivery);
    }

    /**
     * Remove a delivery from the round
     * @param nodeId The targeted delivery node. This node MUST BE a delivery node
     */
    public void removeDelivery(int nodeId) {
        Delivery delivery = findDelivered(nodeId);

        Client client = delivery.getClient();
        Schedule schedule = delivery.getSchedule();

        client.removeDelivery(delivery);
        schedule.removeDelivery(delivery);

        if(schedule.getDeliveries().isEmpty()) {
            schedules.remove(schedule);
        }
    }

    /**
     * Return true if the schedule described is not distinct from the others, false else
     * @param earliestBound the earliest bound
     * @param latestBound the latest bound
     * @return @See description
     */
    public boolean isScheduleOverlapping(GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        for(Schedule s : schedules) {
            if((latestBound.before(s.getLatestBound()) && latestBound.after(s.getEarliestBound()))
                    || (earliestBound.before(s.getLatestBound()) && earliestBound.after(s.getEarliestBound()))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a list of deliveries for all deliveries.
     * This is for the view which will call this to fill its list on the left.
     * @return a vector of strings
     */
    public Vector<Delivery> getDeliveryList() {
        Vector<Delivery> deliveryList = new Vector<Delivery>();
        for(Schedule sched : schedules) {
            for(Delivery deliver : sched.getDeliveries()) {
                deliveryList.add(deliver);
            }
        }

        return deliveryList;
    }

    /**
     * Return the schedule to corresponding to the specified bounds
     * @param earliestBound the earliest bound
     * @param latestBound the latest bound
     * @return the corresponding schedule
     */
    private Schedule getSchedule(GregorianCalendar earliestBound, GregorianCalendar latestBound)
    {
        for(Schedule s : schedules) {
            if(s.getEarliestBound().equals(earliestBound) && s.getLatestBound().equals(latestBound)) {
                return s;
            }
        }

        Schedule schedule = new Schedule(earliestBound, latestBound);
        schedules.add(schedule);

        return schedule;
    }

    /**
     * Return the client corresponding to its ID. If this ID does not exist, a new client is created.
     * @param clientId the client ID
     * @return The client corresponding to the specified ID
     */
    public Client getClient(String clientId)
    {
        for(Client c : clients) {
            if(c.getId().equals(clientId)) {
                return c;
            }
        }

        Client c = new Client(clientId);
        clients.add(c);
        return c;
    }

    /**
     * search a client by its id and return the index of it
     * @param clientId the client id searched
     * @return the index in the list of client, -1 if the id isn't in
     */
    public int getIndexClient(String clientId)
    {
        for(Client c : clients) {
            if(c.getId().equals(clientId)) {
                return clients.indexOf(c);
            }
        }
        return -1;
    }

    /**
     * Returns the network
     * @return the network
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Returns the warehouse
     * @return the warehouse
     */
    public Node getWarehouse() {
        return warehouse;
    }

    /**
     * Returns the round's schedules
     * @return the round's schedules
     */
    public List<Schedule> getSchedules() {
        return schedules;
    }


    /**
     * Returns the clients delivered
     * @return the clients delivered
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     * Fins a delivery from a given node if
     * @param nodeId the node id
     * @return the node if found or null
     */
    public Delivery findDelivered(int nodeId) {
        for(Schedule s : schedules) {
            for(Delivery d :  s.getDeliveries()) {
                if(d.getAddress().getId() == nodeId) {
                    return d;
                }
            }
        }

        return null;
    }

    /**
     * Find a delivery id that is not used yet
     * @return the id
     */
    public int findAnId() {
        int free = Delivery.freeId;
        boolean freeFinded = false;
        while ( ! freeFinded) {
            freeFinded = true;
            for(Schedule s : schedules) {
                for(Delivery d :  s.getDeliveries()) {
                    if(d.getId() == free) {
                        freeFinded = false;
                        free ++;
                        break;
                    }
                }
                if (!freeFinded){
                    break;
                }
            }
        }
        Delivery.freeId = free;
        return free;
    }

     /**
     * Return true if two deliveries deliver the same node, false else
     * @param node The node to check
     * @return @See description
     */
    public boolean isTwiceDelivered(Node node) {
        boolean delivered = false;
        for(Schedule s : schedules) {
            for(Delivery d : s.getDeliveries()) {
                if(d.getAddress().equals(node)) {
                    if(delivered) {
                        return true;
                    }

                    delivered = true;
                }
            }
        }
        return false;
    }

    /**
     * Creates and returns a round from an XML file
     * @param xmlFilePath The xml file path
     * @param network The network containing every nodes
     * @return a round created from the XML file
     * @throws UtilsException If the parsing returns an exception
     * @throws ParserConfigurationException If the XML file contains errors (missing or invalids elements or attributes)
     */
    public static Round createFromXml(String xmlFilePath, Network network) throws UtilsException, ParserConfigurationException, FileNotFoundException {
        Element root;
        Document document;
        DocumentBuilder factory;

        Round round = null;
        Node warehouse;
        List<Delivery> deliveries = new LinkedList<Delivery>();
        File xmlFile = new File(xmlFilePath);

        if(!xmlFile.exists()) {
            throw new FileNotFoundException("Fichier <" + xmlFilePath + "> manquant.");
        }

        try {
            try {
                factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                document = factory.parse(xmlFile);
                root = document.getDocumentElement();
            } catch (ParserConfigurationException pce) {
                throw new UtilsException("Une erreur est survenue lors de l'importation. Merci de contacter les développeurs.");
            } catch (SAXException se) {
                throw new UtilsException("Une erreur est survenue lors de l'importation. Merci de contacter les développeurs.");
            } catch (IOException ioe) {
                throw new UtilsException("LUne erreur est survenue lors de l'ouverture du fichier " + xmlFilePath + ".");
            }

            if (!root.getNodeName().equals(XMLConstants.DELIVERY_ROOT_ELEMENT)) {
                throw new ParserConfigurationException("L'élément racine <"+ XMLConstants.DELIVERY_ROOT_ELEMENT + "> est manquant.");
            }

            round = new Round(network, root);
        }
        catch (UtilsException e) {
            throw  new UtilsException("Fichier <" + xmlFilePath + "> invalide.");
        }

        return round;
    }
}
