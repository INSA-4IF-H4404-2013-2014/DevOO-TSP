package Model.ChocoSolver;

import Model.City.Arc;
import Model.City.Node;
import Model.Delivery.Delivery;
import Model.Delivery.Itinerary;

import java.util.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:34
 * This class aims at managing a round defined by an ordered list of chocoDeliveries
 */
public class CalculatedRound {
    /** The warehouse which is the start and end point of the round */
    private Node warehouse;

    private List<Node> orderedNodes;

    private Map<Integer, ChocoDelivery> chocoDeliveries = new HashMap<Integer, ChocoDelivery>();

    private GregorianCalendar departureTime;

    /** A dictionary linking a delivery to its estimated arrival hour at the delivery point */
    private Dictionary<Integer, GregorianCalendar> estimatedSchedules = new Hashtable<Integer, GregorianCalendar>();

    /**
     * Constructor
     * @param warehouse start and end point of the round
     */
    public CalculatedRound(Node warehouse, int[] tspOrderedDeliveries, ChocoGraph chocoGraph) {
        this.warehouse = warehouse;

        orderedNodes = new ArrayList(tspOrderedDeliveries.length);
        for(int i = 0; i < tspOrderedDeliveries.length; ++i) {
            Integer networkId = chocoGraph.getNetworkIdFromChocoId(tspOrderedDeliveries[i]);
            ChocoDelivery cd = chocoGraph.getChocoDeliveryFromNetworkId(networkId);
            orderedNodes.add(cd.getAddress());
            chocoDeliveries.put(networkId, cd);
        }

        if(orderedNodes.size() != 0) {
//            calculateEstimatedSchedules();
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

        for(Node n : orderedNodes) {
            arrivalTime = getArrivalTime(nodeId, deliveryTime);
            deliveryTime = 600;

            //TODO : CACA
            estimatedSchedules.put(n.getId(), arrivalTime);
        }
    }

    /**
     * Get the departure time at the warehouse
     * @return the departure time at the warehouse
     */
    private GregorianCalendar getFirstDepartureTime() {
        ChocoDelivery firstDelivery = chocoDeliveries.get(getNextNode(warehouse).getId());
        Date departureDate = firstDelivery.getDelivery().getSchedule().getEarliestBound().getTime();

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
     * @param deliveryTime the time spend by the delivery man on the last delivery
     * @return the arrival time at the currentDelivery
     */
    private GregorianCalendar getArrivalTime(int previousNodeId, int deliveryTime) {
        GregorianCalendar arrivalTime;
        Date arrivalDate;
        long timeLapse;

        arrivalTime = (GregorianCalendar) estimatedSchedules.get(previousNodeId).clone();
        arrivalDate = arrivalTime.getGregorianChange();

        // TODO : correct
        // timeLapse = arrivalDate.getTime() + (long) ((getNextItinerary(currentDelivery.).getCost() + deliveryTime) * 1000);

        //arrivalDate.setTime(timeLapse);
        arrivalTime.setTime(arrivalDate);

        return arrivalTime;
    }

    public List<Itinerary> getOrderedItineraries() {
        List<Itinerary> itineraries = new LinkedList<Itinerary>();
        Node currentNode = warehouse;
        Itinerary firstItinerary = getNextItinerary(currentNode), itinerary = firstItinerary;

        if(itinerary != null) {
            do {
                itineraries.add(itinerary);
                currentNode = getNextNode(currentNode);
                itinerary = getNextItinerary(currentNode);
            } while(itinerary != firstItinerary);
        }

        return itineraries;
    }

    /**
     * Returns the estimated delay in milliseconds of a delivery comparing to the latest bound of the schedule
     * @param nodeId The delivery for which the delay is estimated
     * @return The estimated delay in milliseconds
     */
    public long getDelay(Integer nodeId)
    {
        if(nodeId.equals(warehouse.getId())) {
            return 0;
        }

        long delay = estimatedSchedules.get(nodeId).getTimeInMillis() - chocoDeliveries.get(nodeId).getDelivery().getSchedule().getLatestBound().getTimeInMillis();

        if(delay < 0) {
            return 0;
        }

        return delay;
    }

    public long getCumulatedDelay() {
        long roundDelay = 0;

        for(Node n : orderedNodes) {
            roundDelay += getDelay(n.getId());
        }

        return roundDelay;
    }

    public float getTotalLength() {
        float length = 0;
        Itinerary itinerary;

        for(int i = 0; i < orderedNodes.size(); ++i) {
            itinerary = chocoDeliveries.get(orderedNodes.get(i)).getItinerary(orderedNodes.get((i + 1) % orderedNodes.size()).getId());
            length += itinerary.getLength();
        }

        return length;
    }

    public long getTotalDuration() {
        return estimatedSchedules.get(warehouse.getId()).getTimeInMillis() - departureTime.getTimeInMillis();
    }

    public Node getWarehouse() {
        return warehouse;
    }

    public Node getNextNode(Node node) {
        int idx = orderedNodes.indexOf(node);
        return orderedNodes.get((idx + 1) % orderedNodes.size());
    }

    /**
     * Returns the itinerary to cross from a specified delivery node (or the warehouse) in order to go to the next one,
     * or null if the specified node is not a delivery node or the warehouse
     * @param node A delivery node, or the warehouse
     * @return @see description
     */
    public Itinerary getNextItinerary(Node node)
    {
        return chocoDeliveries.get(node.getId()).getItinerary(getNextNode(node).getId());
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
     * @param nodeId the specified delivery
     * @return true if the arrival hour at the specified delivery if after the latest bound of its schedule, false else
     */
    public boolean isDelayed(Integer nodeId)
    {
        if(nodeId.equals(warehouse.getId())) {
            return false;
        }

        GregorianCalendar estimatedSchedule = estimatedSchedules.get(nodeId);

        return estimatedSchedule.after(chocoDeliveries.get(nodeId).getDelivery().getSchedule().getLatestBound());
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
        Node currentNode = warehouse;
        Itinerary firstItinerary = getNextItinerary(currentNode), itinerary = firstItinerary;

        if(itinerary != null) {
            do {
                arcList = itinerary.getArcs();
                delivery = chocoDeliveries.get(currentNode.getId()).getDelivery();

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

                currentNode = getNextNode(currentNode);
                itinerary = getNextItinerary(currentNode);
            } while(itinerary != firstItinerary);
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
