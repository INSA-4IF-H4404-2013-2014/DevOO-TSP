package Model.ChocoSolver;

import Model.City.Node;
import Model.Delivery.Delivery;
import Model.Delivery.Itinerary;
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
public class CalculatedRound {
    /** The warehouse which is the start and end point of the round */
    private Node warehouse;

    /** An ordered list of deliveries which defines the round */
    private List<Delivery> orderedDeliveries;

    /** An ordered list of itineraries in ordered to link the deliveries */
    private List<Itinerary> orderedItineraries = new LinkedList<Itinerary>();

    /** A dictionary linking a delivery to its estimated arrival hour at the delivery point */
    private Dictionary<Delivery, GregorianCalendar> estimatedSchedules = new Hashtable<Delivery, GregorianCalendar>();

    /**
     * Constructor
     * @param warehouse start and end point of the round
     * @param orderedDeliveries an ordered list of deliveries which defines the round
     * @param orderedItineraries an ordered list of itineraries in ordered to link the deliveries
     */
    public CalculatedRound(Node warehouse, List<Delivery> orderedDeliveries, List<Itinerary> orderedItineraries) {
        this.warehouse = warehouse;
        this.orderedDeliveries = orderedDeliveries;
        this.orderedItineraries = orderedItineraries;
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
     * Returns the ordered list of itineraries
     * @return the ordered list of itineraries
     */
    public List<Itinerary> getOrderedItineraries() {
        return orderedItineraries;
    }

    /**
     * Returns the itinerary to cross from a specified delivery node (or the warehouse) in order to go to the next one,
     * or null if the specified node is not a delivery node or the warehouse
     * @param node A delivery node, or the warehouse
     * @return @see description
     */
    public Itinerary getNextItinerary(Node node)
    {
        int i = 0;

        if(node.equals(warehouse)) {
            return orderedItineraries.get(i);
        }

        ++i;

        for(Delivery d : orderedDeliveries) {
            if(d.getAddress().equals(node))
            {
                return orderedItineraries.get(i);
            }
            ++i;
        }

        return null;
    }

    /**
     * Returns the itinerary to cross from a specified delivery in order to go to the next one,
     * or null if the specified node is not a delivery
     * @param delivery A delivery
     * @return @see description
     */
    public Itinerary getNextItinerary(Delivery delivery)
    {
        return getNextItinerary(delivery.getAddress());
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
     * Returns true if the arrival hour at the specified delivery if after the latest bound of its schedule, false else
     * @param delivery the specified delivery
     * @return true if the arrival hour at the specified delivery if after the latest bound of its schedule, false else
     */
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
