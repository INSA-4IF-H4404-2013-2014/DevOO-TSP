package Model.Delivery;

import Model.City.Arc;
import Model.City.Node;

import java.util.List;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 28/11/13
 * Time: 14:34
 * This class aims at managing an itinerary which is defined by an ordered list of arcs linking a start and end node
 */
public class Itinerary {
    /** The start node of the itinerary */
    private Node start;

    /** The end node of the itinerary */
    private Node end;

    /** The ordered list of arcs linking the start and end node */
    private List<Arc> arcs;

    /**
     * Constructor
     * @param start start node of the itinerary
     * @param end end node of the itinerary
     * @param arcs arcs from start node to end node
     */
    public Itinerary(Node start, Node end, List<Arc> arcs) {
        this.start = start;
        this.end = end;
        this.arcs = arcs;
    }

    /**
     * Adds and arc at the end of the itinerary
     * @param arc the arc to add
     */
    public void addArc(Arc arc)
    {
        arcs.add(arc);
    }

    /**
     * Returns the itinerary's start node
     * @return the itinerary's start node
     */
    public Node getStart() {
        return start;
    }

    /**
     * Returns the itinerary's end node
     * @return the itinerary's end node
     */
    public Node getEnd() {
        return end;
    }

    /**
     * Returns the itinerary's ID
     * @return the itinerary's ID
     */
    public List<Arc> getArcs() {
        return arcs;
    }

    /**
     * Get the cost of the itinerary in seconds
     * @return the cost of the itinerary
     */
    public int getCost() {
        int cost = 0;
        for(Arc arc:this.arcs) {
            cost += arc.getCost();
        }

        return cost;
    }
}

