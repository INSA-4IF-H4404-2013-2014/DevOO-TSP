package Model.City;

import java.util.List;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public class Street {

    /** the street's name */
    private String name;

    /** the street's arcs */
    private List<Arc> arcs;

    /**
     * Constructor
     * @param name the street's name
     */
    protected Street(String name)
    {
        this.name = name;
        this.arcs = new LinkedList<Arc>();
    }

    /**
     * Gets the street's name
     * @return the street's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the street's arcs list
     * @return the street's arcs list
     */
    public List<Arc> getArcs() {
        return arcs;
    }

    /**
     * Creates a new street's arc
     * @param from the arc's leaving node
     * @param to the arc's destination node
     * @param length the arc's length
     * @param speed the arc's speed
     * @return the arc that has just been created
     */
    public Arc createArc(Node from, Node to, float length, float speed){
        Arc arc = new Arc(this, from, to, length, speed);

        this.arcs.add(arc);

        return arc;
    }
}
