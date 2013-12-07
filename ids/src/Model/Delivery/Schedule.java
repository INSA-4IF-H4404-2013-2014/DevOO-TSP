package Model.Delivery;

import Model.City.Network;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
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
        NodeList xmlNodeList;
        Calendar currentDate = GregorianCalendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        this.earliestBound = getScheduleBoundFromXMLAttr(element.getAttribute(XMLConstants.DELIVERY_SCHEDULE_EARLIEST_ATTR), year, month, day);
        this.latestBound = getScheduleBoundFromXMLAttr(element.getAttribute(XMLConstants.DELIVERY_SCHEDULE_LATEST_ATTR), year, month, day);

        Element eDeliveries = (Element) element.getElementsByTagName(XMLConstants.DELIVERY_DELIVERIES_ELEMENT);
        if(eDeliveries == null) {
            throw new ParserConfigurationException("L'élément <" + XMLConstants.DELIVERY_DELIVERIES_ELEMENT + "> est introuvable.");
        }

        xmlNodeList = eDeliveries.getElementsByTagName(XMLConstants.DELIVERY_DELIVERY_ELEMENT);
        if(xmlNodeList == null || xmlNodeList.getLength() < 1) {
            throw new ParserConfigurationException("L'élément <" + XMLConstants.DELIVERY_DELIVERY_ELEMENT + "> est introuvable ou ne contient aucune livraison.");
        }

        for (int i = 0; i < xmlNodeList.getLength(); ++i) {
            deliveries.add(new Delivery(round, (Element) xmlNodeList.item(i)));
        }
    }

    /**
     * Returns a gregorian calendar object from a string representing a time in day (8:0:19 or 23:53:1 for example)
     * @param attrValue The string to parse
     * @param year The current year
     * @param month The current month
     * @param day The current day of month
     * @return a gregorian calendar object (@See description)
     * @throws ParserConfigurationException If the parsing fails
     */
    private static GregorianCalendar getScheduleBoundFromXMLAttr(String attrValue, int year, int month, int day) throws ParserConfigurationException {
        int hour, min, sec;
        String [] fields;
        try {
            fields = attrValue.split(XMLConstants.DELIVERY_SCHEDULE_FIELDS_SEPARATOR);
            hour = Integer.parseInt(fields[0]);
            min = Integer.parseInt(fields[1]);
            sec = Integer.parseInt(fields[2]);
            return new GregorianCalendar(year, month, day, hour, min, sec);
        }
        catch (Exception e) {
            throw new ParserConfigurationException("L'attribut <" + XMLConstants.DELIVERY_SCHEDULE_EARLIEST_ATTR + " de l'élément <" + XMLConstants.DELIVERY_SCHEDULE_ELEMENT + "> est manquant ou erroné (hh:mm:ss).");
        }
    }

    /**
     * Adds a delivery to the deliveries list
     * @param delivery the delivery to add
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
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
}
