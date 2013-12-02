package Model.City;

import Utils.Utils;
import Utils.UtilsException;
import org.w3c.dom.Element;

import java.util.List;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public class Node {

    /** the node's id */
    private int id;

    /** the node's X position */
    private int x;

    /** the node's T position */
    private int y;

    /** the node's outgoing arcs */
    private List<Arc> outgoing;

    /**
     * Constructor
     * @param id the node's id
     * @param x the node's X position
     * @param y the node's Y position
     */
    protected Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.outgoing = new LinkedList<Arc>();
    }

    /**
     * Gets the node's id
     * @return the node's id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the node's X position
     * @return the node's X position
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the node's Y position
     * @return the node's Y position
     */
    public int getY() {
        return y;
    }

    /**
     * Gets node's outgoing arcs
     * @return node's outgoing arcs list
     */
    public List<Arc> getOutgoing() {
        return outgoing;
    }
}
