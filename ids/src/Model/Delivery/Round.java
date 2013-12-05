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
 * This class aims at managing a round defined by a list of schdules containing deliveries
 */
public class Round {
    /** The warehouse which is the start and end point of the round */
    private Node warehouse;

    /** The schedules containing the deliveries */
    private List<Schedule> schedules = new LinkedList<Schedule>();

    /**
     * Constructor
     * @param warehouse start and end point of the round
     * @param schedules list of schedules containing the deliveries
     */
    public Round(Node warehouse, List<Schedule> schedules) {
        this.warehouse = warehouse;
        this.schedules = schedules;
    }

    public void addDelivery(String clientId, Node address, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        Client client = getClient(clientId);
        Schedule schedule = getSchedule(earliestBound, latestBound);
        Delivery delivery = new Delivery(client, address, schedule);
        client.addDelivery(delivery);
        schedule.addDelivery(delivery);
        schedules.add(schedule);
    }

    private Schedule getSchedule(GregorianCalendar earliestBound, GregorianCalendar latestBound)
    {
        for(Schedule s : schedules)
        {
            if(s.getEarliestBound().equals(earliestBound) && s.getLatestBound().equals(latestBound)) {
                return s;
            }
        }

        return new Schedule(earliestBound, latestBound);
    }

    private Client getClient(String clientId)
    {
        for(Schedule s : schedules)
        {
            for(Delivery d :  s.getDeliveries())
            {
                if(d.getClient().getId().equals(clientId)) {
                    return d.getClient();
                }
            }
        }

        return new Client(clientId);
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

    public static Round createFromXml(String xmlFilePath) throws NoSuchFieldException {
        Round round = null;
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

        //TODO ============================================================================

        return round;
    }
}
