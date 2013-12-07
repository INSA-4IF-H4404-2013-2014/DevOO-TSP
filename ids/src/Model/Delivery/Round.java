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
import java.io.IOException;
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
            schedules.add(new Schedule(this, (Element) xmlNodeList.item(i)));
        }
    }

    public void addDelivery(String clientId, Node address, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        Client client = getClient(clientId);
        Schedule schedule = getSchedule(earliestBound, latestBound);
        Delivery delivery = new Delivery(schedule.getNextDeliveryId(), client, address, schedule);
        client.addDelivery(delivery);
        schedule.addDelivery(delivery);
        schedules.add(schedule);
    }

    private Schedule getSchedule(GregorianCalendar earliestBound, GregorianCalendar latestBound)
    {
        for(Schedule s : schedules) {
            if(s.getEarliestBound().equals(earliestBound) && s.getLatestBound().equals(latestBound)) {
                return s;
            }
        }

        return new Schedule(earliestBound, latestBound);
    }

    public Client getClient(String clientId)
    {
        for(Schedule s : schedules) {
            for(Delivery d :  s.getDeliveries()) {
                if(d.getClient().getId().equals(clientId)) {
                    return d.getClient();
                }
            }
        }

        return new Client(clientId);
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

    public boolean isDelivered(Node node) {
        for(Schedule s : schedules) {
            for(Delivery d :  s.getDeliveries()) {
                if(d.getAddress().equals(node)) {
                    return true;
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
    public static Round createFromXml(String xmlFilePath, Network network) throws UtilsException, ParserConfigurationException {
        Element root;
        Document document;
        DocumentBuilder factory;

        Round round = null;
        Node warehouse;
        List<Delivery> deliveries = new LinkedList<Delivery>();
        File xmlFile = new File(xmlFilePath);

        if(!xmlFile.exists()) {
            throw new ParserConfigurationException("Fichier <" + xmlFilePath + "> manquant.");
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
