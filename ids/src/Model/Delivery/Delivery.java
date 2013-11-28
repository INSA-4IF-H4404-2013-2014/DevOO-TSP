package Model.Delivery;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:34
 * This class aims at managing a delivery defined by a delivery point (the client's address)
 * and a time frame
 */
public class Delivery {
    /** The delivery unique ID */
    private int id;

    /** The client who has to be delivered */
    private Client client;

    /** The delivery's time frame */
    private Schedule schedule;

    /**
     * Constructor
     * @param id unique ID
     * @param client client who has to be delivered
     * @param schedule delivery's time frame
     */
    public Delivery(int id, Client client, Schedule schedule) {
        this.id = id;
        this.client = client;
        this.schedule = schedule;
    }

    /**
     * Returns the delivery's ID
     * @return the delivery's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the delivery's client
     * @return the delivery's client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Returns the delivery's schedule
     * @return the delivery's schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }
}
