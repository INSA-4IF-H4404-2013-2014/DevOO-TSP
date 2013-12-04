package Model.Delivery;

import Model.City.Node;
import Utils.UtilsException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
 * This class aims at managing a round defined by an ordered list of itineraries
 */
public class Round {
    /** The warehouse which is the start and end point of the round */
    private Node warehouse;

    /** An ordered list of deliveries which defines the round */
    private List<Delivery> deliveries;

    /** A dictionary linking a delivery to its estimated arrival hour at the delivery point */
    private Dictionary<Delivery, GregorianCalendar> estimatedSchedules = new Hashtable<Delivery, GregorianCalendar>();

    /** An ordered list of itineraries which defines the round */
    private List<Itinerary> itineraries = new LinkedList<Itinerary>();

    /**
     * Constructor
     * @param warehouse start and end point of the round
     * @param deliveries ordered list of deliveries which defines the round
     */
    public Round(Node warehouse, List<Delivery> deliveries) {
        this.warehouse = warehouse;
        this.deliveries = deliveries;
        initItineraries();
        calculateEstimatedSchedules();
    }

    /**
     * Calculates the estimated schedules based on the deliveries order and the speed and length of the arcs
     * Important : 10 minutes are required in order to deliver a package
     */
    private void calculateEstimatedSchedules()
    {
        //TODO : CALCULATIONS (10 minutes are required in order to deliver a package)
    }

    /**
     * Returns the estimated delay in milliseconds of a delivery comparing to the latest bound of the schedule
     * @param delivery The delivery for which the delay is estimated
     * @return The estimated delay in milliseconds
     */
    public long getDelay(Delivery delivery)
    {
        if(isDelayed(delivery))
        {
            return estimatedSchedules.get(delivery).getTimeInMillis() - delivery.getSchedule().getLatestBound().getTimeInMillis();
        }

        return 0;
    }

    /**
     * Returns the warehouse
     * @return the warehouse
     */
    public Node getWarehouse() {
        return warehouse;
    }

    /**
     * Returns the ordered list of itineraries
     * @return the ordered list of itineraries
     */
    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    /**
     * Initializes the itinerary list by constructing an itinerary between the following nodes
     * This call includes a dijkstra calculus
     */
    private void initItineraries()
    {
        ListIterator<Delivery> iter = deliveries.listIterator();
        Node start = warehouse;
        Node end;

        while(iter.hasNext())
        {
            end = iter.next().getClient().getAddress();
            itineraries.add(new Itinerary(start, end));
            start = end;
        }

        end = warehouse;
        itineraries.add(new Itinerary(start, end));
    }

    public boolean isDelayed(Delivery delivery)
    {
        GregorianCalendar estimatedSchedule = estimatedSchedules.get(delivery);

        if(estimatedSchedule == null)
        {
            return false;
        }

        return estimatedSchedule.after(delivery.getSchedule().getLatestBound());
    }

    public static Round createFromXml(String xmlFilePath) throws NoSuchFieldException {
        Round round;
        Node warehouse;
        List<Delivery> deliveries = new LinkedList<Delivery>();
        File xmlFile = new File(xmlFilePath);

        if(!xmlFile.exists())
        {
            throw new NoSuchFieldException("Fichier " + xmlFilePath + " introuvable.");
        }

        Element root;
        Document document;
        DocumentBuilder factory;

        try {
            try {
                factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                document = factory.parse(fileXml);

                root = document.getDocumentElement();
            } catch (ParserConfigurationException pce) {
                throw new UtilsException("DOM parsor configuration exception");
            } catch (SAXException se) {
                throw new UtilsException("XML parse stage failed");
            } catch (IOException ioe) {
                throw new UtilsException("Error while reading file \"" + xmlPath + "\"");
            }

            if (!root.getNodeName().equals("Reseau")) {
                throw new UtilsException("Unexpected XML root name \"" + root.getNodeName() + "\"");
            }

            graph = new Graph();
            graph.loadNetworkFromXml(root);
        }
        catch (UtilsException e) {
            throw  new UtilsException("File \"" + xmlPath + "\" > " + e);
        }

        return graph;

        Node warehouse = new Node();
        Round round = new Round();
    }
}
