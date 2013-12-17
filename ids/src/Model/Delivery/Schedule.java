package Model.Delivery;

import Model.City.Network;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:34
 * This class aims at managing a schedule defined by a time frame
 */
public class Schedule {
    /** Counter used in order to maintain id unique constraint for deliveries */
    private int nextDeliveryId = 1;

    /** Earliest bound of the time frame - The delivery shall not be delivered before */
    private GregorianCalendar earliestBound;

    /** Latest bound of the time frame - The delivery shall not be delivered after (delay) */
    private GregorianCalendar latestBound;

    /** A list of deliveries */
    private List<Delivery> deliveries = new LinkedList<Delivery>();

    /**
     * Constructor
     * @param earliestBound Earliest bound of the time frame
     * @param latestBound Latest bound of the time frame
     */
    public Schedule(GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        this.earliestBound = earliestBound;
        this.latestBound = latestBound;
    }

    /**
     * Constructor which parses XML nodes and attributes
     * @param round Round containing every schedules
     * @param element Schedule XML element
     * @throws ParserConfigurationException If the parsing fails
     */
    public Schedule(Round round, Element element) throws ParserConfigurationException {
        Calendar currentDate = GregorianCalendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        this.earliestBound = getScheduleBoundFromXMLAttr(element, XMLConstants.DELIVERY_SCHEDULE_EARLIEST_ATTR, year, month, day);
        this.latestBound = getScheduleBoundFromXMLAttr(element, XMLConstants.DELIVERY_SCHEDULE_LATEST_ATTR, year, month, day);

        if(this.earliestBound.after(this.latestBound)) {
           throw new ParserConfigurationException("L'heure de début de l'élément <" + XMLConstants.DELIVERY_DELIVERIES_ELEMENT + "> est postérieure à son heure de fin.");
        }

        if(round.isScheduleOverlapping(this.earliestBound, this.latestBound)) {
            throw new ParserConfigurationException("Un chevauchement de plages horaires a été détecté.");
        }

        NodeList deliveries = element.getElementsByTagName(XMLConstants.DELIVERY_DELIVERIES_ELEMENT);
        if(deliveries == null || deliveries.getLength() == 0) {
            throw new ParserConfigurationException("L'élément <" + XMLConstants.DELIVERY_DELIVERIES_ELEMENT + "> est manquant.");
        }

        Element eDeliveries = (Element) deliveries.item(0);
        NodeList delivery = eDeliveries.getElementsByTagName(XMLConstants.DELIVERY_DELIVERY_ELEMENT);
        for (int i = 0; i < delivery.getLength(); ++i) {
            this.deliveries.add(new Delivery(round, this, (Element) delivery.item(i)));
            nextDeliveryId += 1;
        }
    }

    /**
     * Returns a gregorian calendar object from a string representing a time in day (8:0:19 or 23:53:1 for example)
     * @param element The element to get the attribute from
     * @param attributeName The attribute's name we have to parse the value
     * @param year The current year
     * @param month The current month
     * @param day The current day of month
     * @return a gregorian calendar object (@See description)
     * @throws ParserConfigurationException If the parsing fails
     */
    private static GregorianCalendar getScheduleBoundFromXMLAttr(Element element, String attributeName, int year, int month, int day) throws ParserConfigurationException {
        int hour, min, sec;
        String [] fields;
        String attributeValue;

        attributeValue = element.getAttribute(attributeName);
        fields = attributeValue.split(XMLConstants.DELIVERY_SCHEDULE_FIELDS_SEPARATOR);
        if(fields.length != 3) {
            throw new ParserConfigurationException("L'attribut <" + attributeName + "> de l'élément <" + XMLConstants.DELIVERY_SCHEDULE_ELEMENT + "> est manquant ou ne contient pas le bon nombre de champs (3 requis).");
        }

        try {
            hour = Integer.parseInt(fields[0]);
            min = Integer.parseInt(fields[1]);
            sec = Integer.parseInt(fields[2]);
            return new GregorianCalendar(year, month, day, hour, min, sec);
        } catch (Exception e) {
            throw new ParserConfigurationException("Un champ de l'attribut <" + attributeName + "> de l'élément <" + XMLConstants.DELIVERY_SCHEDULE_ELEMENT + "> ne contient pas un entier.");
        }
    }

    /**
     * Adds a delivery to the deliveries list
     * @param delivery the delivery to add
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        nextDeliveryId += 1;
    }

    /**
     * Removes a delivery to the deliveries list
     * @param delivery the delivery to remove
     */
    public void removeDelivery(Delivery delivery) {
        deliveries.remove(delivery);
    }

    /**
     * Returns the schedule's deliveries
     * @return the schedule's deliveries
     */
    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    /**
     * Returns the earliest bound of the time frame
     * @return the earliest bound of the time frame
     */
    public GregorianCalendar getEarliestBound() {
        return earliestBound;
    }

    /**
     * Returns the latest bound of the time frame
     * @return the latest bound of the time frame
     */
    public GregorianCalendar getLatestBound() {
        return latestBound;
    }

    /**
     * Returns the next unique delivery id for this schedule
     * @return the next unique delivery id for this schedule
     */
    public int getNextDeliveryId() {
        return nextDeliveryId;
    }
}
