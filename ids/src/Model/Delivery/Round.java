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

    /** The clients delivered by the round */
    private List<Client> clients = new LinkedList<Client>();

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

    public void addDelivery(String clientId, Node address, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        Client client = null, tmpClient;
        boolean found = false;
        ListIterator<Client> iter = clients.listIterator();

        while(iter.hasNext() && !found)
        {
            tmpClient = iter.next();
            if(tmpClient.getId().equals(clientId)) {
                found = true;
                client = tmpClient;
            }
        }

        if(!found) {
            client = new Client(clientId);
        }

        deliveries.add(new Delivery(client, address, new Schedule(earliestBound, latestBound)));
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
     * Returns the round's deliveries
     * @return the round's deliveries
     */
    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    /**
     * Returns the estimated arrival hour of a delivery
     * @param delivery the specified delivery
     * @return the estimated arrival hour of a delivery
     */
    public GregorianCalendar getEstimatedSchedules(Delivery delivery) {
        return estimatedSchedules.get(delivery);
    }

    /**
     * Initializes the itinerary list by constructing an itinerary between the following nodes
     * This call includes a dijkstra calculus
     */
    private void initItineraries()
    {
        //TODO : This method was developped without thinking about choco
        //TODO : This one may have to be changed
        ListIterator<Delivery> iter = deliveries.listIterator();
        Node start = warehouse;
        Node end;

        while(iter.hasNext())
        {
            end = iter.next().getAddress();
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
}
