package Model.ChocoSolver;

import Model.City.Arc;
import Model.City.Node;
import Model.Delivery.Client;
import Model.Delivery.Delivery;
import Model.Delivery.Itinerary;
import Model.Delivery.Schedule;
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

    private GregorianCalendar departureTime;

    /** An ordered list of deliveries which defines the round */
    private List<Delivery> orderedDeliveries;

    /** An ordered list of itineraries in ordered to link the deliveries */
    private List<Itinerary> orderedItineraries = new LinkedList<Itinerary>();

    /** A dictionary linking a delivery to its estimated arrival hour at the delivery point */
    private Dictionary<Integer, GregorianCalendar> estimatedSchedules = new Hashtable<Integer, GregorianCalendar>();

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
        if(!orderedDeliveries.isEmpty()) {
            calculateEstimatedSchedules();
        }
    }

    /**
     * Calculates the estimated schedules based on the deliveries order and the speed and length of the arcs
     * Important : 10 minutes are required in order to deliver a package
     */
    private void calculateEstimatedSchedules()
    {
        this.departureTime = getFirstDepartureTime();

        GregorianCalendar arrivalTime;

        long timeLapse;
        int deliveryTime = 0;

        int nodeId = warehouse.getId();

        for(Delivery delivery : orderedDeliveries) {

            arrivalTime = getArrivalTime(nodeId, delivery, deliveryTime);

            nodeId = delivery.getAddress().getId();
            deliveryTime = 600;

            //TODO : check that the warehouse is put in the map as last element
            estimatedSchedules.put(nodeId, arrivalTime);
        }
    }

    /**
     * Get the departure time at the warehouse
     * @return the departure time at the warehouse
     */
    private GregorianCalendar getFirstDepartureTime() {
        Date departureDate = orderedDeliveries.get(0).getSchedule().getEarliestBound().getGregorianChange();

        int warehouseToDeliveryTime = getNextItinerary(warehouse).getCost();

        long departureTime = departureDate.getTime() - (long)(warehouseToDeliveryTime * 1000);

        departureDate.setTime(departureTime);

        GregorianCalendar departure = new GregorianCalendar();
        departure.setTime(departureDate);

        // If the departure time is between 00:00am and 06:00am,
        // we force the departure time to 06:00am
        if(departure.get(Calendar.HOUR_OF_DAY) < 6) {
            departure.set(Calendar.HOUR_OF_DAY, 6);
            departure.set(Calendar.MINUTE, 0);
        }

        return departure;
    }

    public GregorianCalendar getDepartureTime() {
        return departureTime;
    }

    public GregorianCalendar getArrivalTime() {
        return estimatedSchedules.get(warehouse.getId());
    }

    /**
     * Get the arrival time of the currentDelivery
     * @param previousNodeId the id of the node that is before the currentDelivery
     * @param currentDelivery the delivery we want the arrival time
     * @param deliveryTime the time spend by the delivery man on the last delivery
     * @return the arrival time at the currentDelivery
     */
    private GregorianCalendar getArrivalTime(int previousNodeId, Delivery currentDelivery, int deliveryTime) {
        GregorianCalendar arrivalTime;
        Date arrivalDate;
        long timeLapse;

        arrivalTime = (GregorianCalendar) estimatedSchedules.get(previousNodeId).clone();
        arrivalDate = arrivalTime.getGregorianChange();

        timeLapse = arrivalDate.getTime() + (long) ((getNextItinerary(currentDelivery).getCost() + deliveryTime) * 1000);

        arrivalDate.setTime(timeLapse);
        arrivalTime.setTime(arrivalDate);

        return arrivalTime;
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

    public long getCumulatedDelay() {
        long roundDelay = 0;

        for(Delivery d : orderedDeliveries) {
            roundDelay += getDelay(d);
        }

        return roundDelay;
    }

    public float getTotalLength() {
        float length = 0;

        for(Itinerary i : orderedItineraries) {
            length += i.getLength();
        }

        return length;
    }

    public long getTotalDuration() {
        return estimatedSchedules.get(warehouse.getId()).getTimeInMillis() - departureTime.getTimeInMillis();
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


    // TODO: to be tested!  to be prune !!!
    /**
     * Parse the round into a html format
     * @return the html text in a String
     */
    public String calculatedRoundToHtml() {

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

        int i = 0;

        html = htmlOpen;

        html += hOpen + "Départ de l'entrepôt à " + getFirstDepartureTime().get(Calendar.HOUR_OF_DAY) + "h" + getFirstDepartureTime().get(Calendar.MINUTE) + hClose;

        List<Arc> arcList;
        Delivery delivery;

        for(Itinerary itinerary:orderedItineraries) {
            arcList = itinerary.getArcs();
            delivery = orderedDeliveries.get(i++);

            html += tableOpen;
            for(Arc arc:arcList) {
                html += tdOpen;

                //TODO: add "Turn left/right/etc..." to indications (use Arc.getDirection(Arc arc))

                html += trOpen + "Prendre rue " + arc.getStreet().getName() + trClose;
                html += trOpen + arc.getLength() + "m" + trClose;
                html += trOpen + arc.getCost()/60 + "min" + trClose;
                html += tdClose;
            }
            html += tableClose;
            html += hOpen + "Client n°" + delivery.getClient().getId() + hClose;
            html += hOpen + "Plage horaire : " + delivery.getSchedule().getEarliestBound() +" | " + delivery.getSchedule().getLatestBound() + hClose;
        }

        html += htmlClose;

        return html;
    }

    /**
     * convert from MILISECONDS (thank you remi) to hours:minutes
     * @param milisec delay in MILISECONDS (for tyrant supervisor)
     * @return a string containing hours:minutes (yes we loose some precision)
     */
    public static String conversionMSHM(long milisec)
    {
        String res="";
        long hour, min;

        hour = milisec/3600000;
        min = (milisec-(hour*3600000))/60000;
        res += hour + ":" + min;
        return res;
    }
}
