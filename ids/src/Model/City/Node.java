package Model.City;

import Utils.Utils;
import Utils.UtilsException;
import org.w3c.dom.Element;

import java.util.List;
import java.util.LinkedList;
import java.util.TreeSet;

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

    /** the node's X position (in meters) */
    private int x;

    /** the node's Y position (in meters) */
    private int y;

    /** the node's outgoing arcs */
    private List<Arc> outgoing;

    /** the node's incoming arcs */
    private List<Arc> incoming;

    /** Computed string representation */
    private String computedStringRepresentation;

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
        this.incoming = new LinkedList<Arc>();
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

    /**
     * Gets node's incoming arcs
     * @return node's incoming arcs list
     */
    public List<Arc> getIncoming() {
        return incoming;
    }

    /**
     * Gets the outgoing arc to a given node id
     * @param destinationId the destination node's id
     * @return
     *  - null if no such arc exists
     *  - the arc going to destination node
     */
    public Arc findOutgoingTo(int destinationId) {
        for(Arc outgoingArc : this.outgoing) {
            if (outgoingArc.getTo().getId() == destinationId) {
                return outgoingArc;
            }
        }

        return null;
    }

    /**
     * Gets the outgoing arc to a given node
     * @param node the destination node
     * @return
     *  - null if no such arc exists
     *  - the arc going to destination node
     */
    public Arc findOutgoingTo(Node node) {
        return findOutgoingTo(node.getId());
    }

    /**
     * Returns true if an object is equals to the specified node, false else
     * The object is equal if its class type is Node, and if its ID, X and Y coordinates are the same
     * @param o the object to compare to the current
     * @return @See description
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Node)) {
            return false;
        }

        Node n = (Node)o;

        return n.getId() == id && n.getX() == x && n.getY() == y;
    }

    /**
     * Returns an hash code which is the node id
     * @return @See description
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Represents a node in a string.
     * If the node is at a crossroads, the string will contain all street's names.
     * @return The string
     */
    public String toString() {
        if(computedStringRepresentation != null) {
            return computedStringRepresentation;
        }

        TreeSet<String> streetsNames = new TreeSet<String>();

        for(Arc out : outgoing) {
            streetsNames.add(out.getStreet().getName());
        }

        for(Arc in : incoming) {
            streetsNames.add(in.getStreet().getName());
        }

        if(streetsNames.size() == 1) {
            computedStringRepresentation = "rue " + streetsNames.first();
        }
        else {
            computedStringRepresentation = new String("entre les rues ");
            int pos = 0;
            for(String streetName : streetsNames) {
                if(streetName == streetsNames.last()) {
                    computedStringRepresentation += " et ";
                }

                computedStringRepresentation += streetName;

                if(pos < streetsNames.size() - 2) {
                    computedStringRepresentation += ", ";
                }
                ++pos;
            }
        }
        return computedStringRepresentation;
    }
}
