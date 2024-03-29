package model.chocosolver;

import model.city.Arc;
import model.city.Node;
import model.delivery.Delivery;
import model.delivery.Itinerary;

import java.util.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:34
 * This class is dedicated to provide information about the delivery order, the itineraries to follow and the
 * estimated arrival hour and delays
 */
public class CalculatedRound {
    /** The warehouse which is the start and end point of the round */
    private Node warehouse;

    /**
     * Key : node ID
     * Value : successors node ID
     * Note : The node ID can be the warehouse's one
     */
    private Map<Integer, Integer> successors = new HashMap<Integer, Integer>();

    /**
     * Key : node ID
     * Value : ChocoDelivery containing the delivery (null for the warehouse) and the itinerary for the successors nodes
     * Note : The node ID can be the warehouse's one
     */
    private Map<Integer, ChocoDelivery> chocoDeliveries = new HashMap<Integer, ChocoDelivery>();

    /** The round departure time */
    private GregorianCalendar departureTime;

    /**
     * Key : node ID
     * Value : estimated arrival hour at the node (which is a delivery point or the warehouse)
     * Note : The node ID can be the warehouse's one
     */
    private Map<Integer, GregorianCalendar> estimatedSchedules = new Hashtable<Integer, GregorianCalendar>();

    /**
     * Constructor which estimates the arrival hour at every delivery, and for the warehouse
     * @param warehouse The warehouse
     * @param tspOrderedDeliveries The successors "dictionary" nodes returned by the TSP (@see tsp.getNext())
     * @param chocoGraph The calculated choco graph
     */
    public CalculatedRound(Node warehouse, int[] tspOrderedDeliveries, ChocoGraph chocoGraph) {
        this.warehouse = warehouse;

        this.chocoDeliveries = chocoGraph.getDeliveries();

        if(tspOrderedDeliveries != null && tspOrderedDeliveries.length != 0) {
            for(int i = 0; i < tspOrderedDeliveries.length; ++i) {
                Integer srcId = chocoGraph.getNetworkIdFromChocoId(i);
                Integer destId = chocoGraph.getNetworkIdFromChocoId(tspOrderedDeliveries[i]);
                successors.put(srcId, destId);
            }
        }

        calculateEstimatedSchedules();
    }

    /**
     * Calculates the estimated schedules based on the deliveries order and the speed and length of the arcs
     * Note : 10 minutes are required in order to deliver a package
     */
    private void calculateEstimatedSchedules()
    {
        int warehouseId = getWarehouse().getId();
        departureTime = getFirstDepartureTime();

        if(successors.size() != 0) {
            GregorianCalendar arrivalTime;
            GregorianCalendar earlyBound;

            // Initialisation
            int nextId = getNextNodeId(warehouseId);
            estimatedSchedules.put(nextId, getArrivalTime(warehouseId, 0));
            int previousId = nextId;

            for(nextId = getNextNodeId(previousId) ; nextId != warehouseId ; nextId = getNextNodeId(previousId))
            {
                arrivalTime = getArrivalTime(previousId, 600);
                earlyBound = chocoDeliveries.get(nextId).getDelivery().getSchedule().getEarliestBound();

                if(arrivalTime.getTime().getTime() < earlyBound.getTime().getTime()) {
                    estimatedSchedules.put(nextId, earlyBound);
                } else {
                    estimatedSchedules.put(nextId, arrivalTime);
                }
                previousId = nextId;
            }

            estimatedSchedules.put(warehouseId, getArrivalTime(previousId, 600));
        }
        else {
            estimatedSchedules.put(warehouseId, departureTime);
        }
    }


    /**
     * Calculates the departure time from the warehouse
     * @return the departure time from the warehouse
     */
    private GregorianCalendar getFirstDepartureTime() {
        GregorianCalendar departure;
        ChocoDelivery firstDelivery = chocoDeliveries.get(getNextNodeId(warehouse.getId()));

        if(firstDelivery != null) {
            Date departureDate = firstDelivery.getDelivery().getSchedule().getEarliestBound().getTime();

            int warehouseToDeliveryTime = getNextItinerary(warehouse.getId()).getCost();

            long departureTime = departureDate.getTime() - (long)(warehouseToDeliveryTime * 1000);

            departureDate.setTime(departureTime);

            departure = new GregorianCalendar();
            departure.setTime(departureDate);

            // If the departure time is between 00:00am and 06:00am,
            // we force the departure time to 06:00am
            if(departure.get(Calendar.HOUR_OF_DAY) < 6) {
                departure.set(Calendar.HOUR_OF_DAY, 6);
                departure.set(Calendar.MINUTE, 0);
            }
            return departure;
        }
        else {
            Calendar today = GregorianCalendar.getInstance();
            return new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        }
    }

    /**
     * Convert an ArcView.Direction to an String directly usable in the HTML parser
     * @param direction the direction to translate
     * @return the translation
     */
    private String translateDirections(Arc.Direction direction) {
        if(direction == Arc.Direction.TURN_LEFT) {
            return "Gauche";
        } else if(direction == Arc.Direction.TURN_RIGHT) {
            return "Droite";
        } else if(direction == Arc.Direction.TURN_BACK) {
            return "Demi-tour";
        } else {
            return "Tout droit";
        }
    }

    /**
     * Give the time of an GregorianCalendar using the HH:MM format
     * @param calendar the calendar to "translate"
     * @return HH:MM formated time
     */
    private String calendarToHourMinutes(GregorianCalendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String result = "";

        if(hour < 10) {
            result = "0";
        }

        result += hour + ":";

        if(min < 10) {
            result += "0";
        }

        result += min;
        return result;
    }

    /**
     * Returns the warehouse
     * @return the warehouse
     */
    public Node getWarehouse() {
        return warehouse;
    }

    /**
     * Returns the round departure time from the warehouse
     * @return the round departure time from the warehouse
     */
    public GregorianCalendar getDepartureTime() {
        return departureTime;
    }

    /**
     * Returns the estimated arrival time at the warehouse at the end of the round
     * @return the estimated arrival time at the warehouse
     */
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

        if(previousNodeId == warehouse.getId()) {
            arrivalTime = (GregorianCalendar) departureTime.clone();
        } else {
            arrivalTime = (GregorianCalendar) estimatedSchedules.get(previousNodeId).clone();
        }

        arrivalDate = arrivalTime.getTime();

        timeLapse = arrivalDate.getTime() + (((long) getNextItinerary(previousNodeId).getCost() +  (long) deliveryTime) * 1000);

        arrivalDate.setTime(timeLapse);
        arrivalTime.setTime(arrivalDate);

        return arrivalTime;
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

        GregorianCalendar estimated = estimatedSchedules.get(nodeId);

        if(estimated == null) {
            return 0;
        }

        long delay = estimated.getTimeInMillis() - chocoDeliveries.get(nodeId).getDelivery().getSchedule().getLatestBound().getTimeInMillis();

        if(delay < 0) {
            return 0;
        }

        return delay;
    }

    /**
     * Returns the sum of every delays for the deliveries in milliseconds
     * @return @see description
     */
    public long getCumulatedDelay() {
        long roundDelay = 0;

        for(Integer n : successors.keySet()) {
            roundDelay += getDelay(n);
        }

        return roundDelay;
    }

    /**
     * Returns the total length of a round
     * Note : the measure unit is the same as the XML file which has been used for importing the round
     * @return @see description
     */
    public float getTotalLength() {
        float length = 0;
        Itinerary itinerary;

        for(Integer i : successors.keySet()) {
            itinerary = chocoDeliveries.get(i).getItinerary(successors.get(i));
            length += itinerary.getLength();
        }

        return length;
    }

    /**
     * Returns the estimated duration of this round
     * @return @see description
     */
    public long getTotalDuration() {
        return estimatedSchedules.get(warehouse.getId()).getTimeInMillis() - departureTime.getTimeInMillis();
    }

    /**
     * Returns the successor of the specified node
     * @param nodeId the node we want the successor
     * @return @see description
     */
    public Integer getNextNodeId(Integer nodeId) {
        return successors.get(nodeId);
    }

    /**
     * Returns the itinerary to cross from a specified delivery node (or the warehouse) in order to go to the next one,
     * or null if the specified node is not a delivery node or the warehouse
     * @param nodeId A delivery node id, or the warehouse id
     * @return @see description
     */
    public Itinerary getNextItinerary(Integer nodeId)
    {
        return chocoDeliveries.get(nodeId).getItinerary(getNextNodeId(nodeId));
    }

    /**
     * An ordered list of successors nodes ID. The first node is the warehouse, the second the warehouse's successor...
     * The last node is the one before returning to the warehouse (last delivery)
     * @return @see description
     */
    public List<Integer> getOrderedNodesId() {
        List<Integer> nodesId = new LinkedList<Integer>();
        Integer currentNodeId = warehouse.getId();

        do {
            nodesId.add(currentNodeId);
            currentNodeId = successors.get(currentNodeId);
        } while(currentNodeId != null && warehouse.getId() != currentNodeId);

        return nodesId;
    }

    /**
     * Returns the deliveries + warehouse ID without any order consideration
     * @return @See description
     */
    public List<Integer> getNodesId() {
        List nodesId =  new LinkedList<Integer>();
        nodesId.addAll(chocoDeliveries.keySet());
        return nodesId;
    }

    /**
     * Returns a TSP defined by an ordered itineraries list, from the warehouse to the warehouse, goind to every delivery node
     * @return @see description
     */
    public List<Itinerary> getOrderedItineraries() {
        List<Itinerary> itineraries = new LinkedList<Itinerary>();

        if(successors.size() != 0) {
            Integer currentNodeId = warehouse.getId();
            Itinerary firstItinerary = getNextItinerary(currentNodeId), itinerary = firstItinerary;

            if(itinerary != null) {
                do {
                    itineraries.add(itinerary);
                    currentNodeId = getNextNodeId(currentNodeId);
                    itinerary = getNextItinerary(currentNodeId);
                } while(itinerary != firstItinerary);
            }
        }
        return itineraries;
    }

    /**
     * Returns the estimated arrival hour of a delivery
     * @param nodeId the specified node id for the delivery
     * @return the estimated arrival hour of a delivery
     */
    public GregorianCalendar getEstimatedSchedules(Integer nodeId) {
        return estimatedSchedules.get(nodeId);
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

        if(estimatedSchedule == null) {
            return false;
        }

        return estimatedSchedule.after(chocoDeliveries.get(nodeId).getDelivery().getSchedule().getLatestBound());
    }

    /**
     * Parse the round into a html format
     * @return the html text in a String
     */
    public String calculatedRoundToHtml() {

        String htmlOpen = "<html>\n";
        String htmlClose = "\n</html>";

        String tableOpen = "\n<table>\n";
        String tableClose = "\n</table>\n";

        String hOpen = "\n<h5>";
        String hClose = "</h5>\n";

        String trOpen = "\n<tr>\n";
        String trClose = "\n</tr>\n";

        String tdOpen = "\n<td>";
        String tdClose = "</td><br/>\n";

        String html = htmlOpen;
        html += "<head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" /></head>\n<body>";

        html += hOpen + "D&eacute;part de l'entrep&ocirc;t &agrave; " + calendarToHourMinutes(departureTime) + hClose;

        List<Arc> arcList;
        Delivery delivery;
        Integer currentNodeId = warehouse.getId();
        Itinerary firstItinerary = getNextItinerary(currentNodeId), itinerary = firstItinerary;
        currentNodeId = getNextNodeId(currentNodeId);
        GregorianCalendar departure;

        int i;

        if(itinerary != null) {
            do {
                i = 0;
                arcList = itinerary.getArcs();
                delivery = chocoDeliveries.get(currentNodeId).getDelivery();

                html += tableOpen;
                for(Arc arc:arcList) {
                    html += tdOpen;

                    if(i == 0) {
                        html += trOpen + "Prendre rue " + arc.getStreet().getName() + trClose;
                        i++;
                    } else {
                        html += trOpen + translateDirections(arcList.get(i-1).getDirectionTo(arc)) + " sur rue " + arc.getStreet().getName() + trClose;
                        i++;
                    }

                    html += trOpen + "sur " + arc.getLength() + "m" + trClose;
                    html += trOpen + "(temps estimé : " + arc.getCost()/60 + "min)" + trClose;
                    html += tdClose;
                }


                if(currentNodeId != warehouse.getId()) {
                    html += hOpen + "Client n&deg;" + delivery.getClient().getId() + "<br/>";
                    html += "Adresse : " + delivery.getAddress() + "<br/>";
                    html += "Livraison entre " + calendarToHourMinutes(delivery.getSchedule().getEarliestBound()) + " et " + calendarToHourMinutes(delivery.getSchedule().getLatestBound()) + "<br/>";
                    html += "Heure d'arrivée estimée : " + calendarToHourMinutes(estimatedSchedules.get(delivery.getAddress().getId())) + "<br/>";

                    departure = (GregorianCalendar) estimatedSchedules.get(delivery.getAddress().getId()).clone();
                    departure.add(Calendar.MINUTE, 10);

                    html += "Heure de départ estimée : " + calendarToHourMinutes(departure) + hClose;
                } else { // If it is the itinerary to return to the warehouse
                    html += hOpen + "Arrivée à l'entrepot vers : " + calendarToHourMinutes(estimatedSchedules.get(warehouse.getId())) + hClose;
                }
                html += tableClose;

                itinerary = getNextItinerary(currentNodeId);
                currentNodeId = getNextNodeId(currentNodeId);
            } while(itinerary != firstItinerary);
        }

        html += "</body>\n" + htmlClose;

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
        res += hour + "h" + min;
        return res;
    }
}
