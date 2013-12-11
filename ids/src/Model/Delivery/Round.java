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

    public void addDelivery(String clientId, int nodeId, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        Node node = network.findNode(nodeId);

        Client client = getClient(clientId);
        Schedule schedule = getSchedule(earliestBound, latestBound);

        Delivery delivery = new Delivery(schedule.getNextDeliveryId(), client, node, schedule);

        client.addDelivery(delivery);
        schedule.addDelivery(delivery);
    }

    public void removeDelivery(int nodeId) {
        Delivery delivery = findDelivered(nodeId);

        Client client = delivery.getClient();
        Schedule schedule = delivery.getSchedule();

        client.removeDelivery(delivery);
        schedule.removeDelivery(delivery);
    }

    /**
     * Gets a list of strings for all deliveries.
     * This is for the view which will call this to fill its list on the left.
     * @return a vector of strings
     */
    public Vector<String> getDeliveryDisplayableList() {
        Vector<String> displayableDeliveryList = new Vector<String>();
        for(Schedule sched : schedules) {
            for(Delivery deliver : sched.getDeliveries()) {
                displayableDeliveryList.add(deliver.toString());
            }
        }

        return displayableDeliveryList;
    }

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

    public boolean isTwiceDelivered(Node node) {
        boolean delivered = false;
        for(Schedule s : schedules) {
            for(Delivery d :  s.getDeliveries()) {
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


    /**
     * Parse the round into a html format
     * @return the html text in a String
     */
    public String roundToHtml() {

        String earlyText = "Heure min : ";
        String latestText = "Heure max : ";

        String htmlOpen = "<html>\n";
        String htmlClose = "\n</html>";

        String tableOpen = "\n<table>\n";
        String tableClose = "\n</table>\n<br/>\n";

        String hOpen = "\n<h5>";
        String hClose = "</h5>\n";

        String trOpen = "\n<tr>\n";
        String trClose = "\n</tr>\n";

        String tdOpen = "\n<td>";
        String tdClose = "</td>\n";

        String html;

        Client client;
        String clientId;
        String adress;

        GregorianCalendar earliestBound;
        GregorianCalendar latestBound;

        int i = 0;

        List<Schedule> schedulesList = this.getSchedules();

        html = htmlOpen;

        for(Schedule schedule:schedulesList) {

            if(schedule.getDeliveries().size() != 0) {

                i++;

                List<Delivery> deliveriesList = schedule.getDeliveries();

                earliestBound = schedule.getEarliestBound();
                latestBound = schedule.getLatestBound();

                html += tableOpen;
                html += hOpen + "Plage horaire n&deg;" + Integer.toString(i) + hClose;
                html += hOpen + earlyText + earliestBound.get(Calendar.HOUR_OF_DAY) + "h" + earliestBound.get(Calendar.MINUTE);
                html += " | " + latestText + latestBound.get(Calendar.HOUR_OF_DAY) + "h" + latestBound.get(Calendar.MINUTE) + hClose;

                for(Delivery delivery:deliveriesList) {
                    client = delivery.getClient();
                    clientId = client.getId();
                    adress = Integer.toString(delivery.getAddress().getId());

                    html += trOpen;

                    html += tdOpen + "Client n&deg; : " + clientId + " | " + tdClose;
                    html += tdOpen + "Lieu n&deg; : " + adress + tdClose;

                    html += trClose;
                }

                html += tableClose;
            }
        }

        html += htmlClose;

        return html;
    }
}
