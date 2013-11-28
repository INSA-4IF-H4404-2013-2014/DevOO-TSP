package Model.Delivery;

import java.util.GregorianCalendar;

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
