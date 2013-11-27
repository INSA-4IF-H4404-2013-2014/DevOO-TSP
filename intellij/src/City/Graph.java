package City;

import java.util.List;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class Graph {

    /** the graph's streets */
    List<Street> streets;

    /** the graph's nodes */
    List<Node> nodes;


    /**
     * Constructs a empty graph
     */
    public Graph() {
        this.streets = new LinkedList<Street>();
        this.nodes = new LinkedList<Node>();
    }

    /**
     * Gets all streets
     * @return streets list
     */
    public List<Street> getStreets() {
        return streets;
    }

    /**
     * Gets all nodes
     * @return nodes list
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Generates a list of all arcs
     * @return arcs list
     */
    public List<Arc> getArcs() {
        List<Arc> arcs = new LinkedList<Arc>();

        for (Street i : this.streets) {
            arcs.addAll(i.getArcs());
        }

        return arcs;
    }

    /**
     * Finds a node with a given id
     * @param id node id
     * @return
     *  - null if there is no node with the given id
     *  - the node that has the given id
     */
    public Node findNode(int id) {
        for (Node i : this.nodes) {
            if (id == i.getId()) {
                return i;
            }
        }

        return null;
    }

    /**
     * Fins a street with a given name
     * @param name the street name
     * @return
     *  - null if there is no street with the given name
     *  - the street that has the given name
     */
    public Street findStreet(String name) {
        for (Street i : this.streets) {
            if (name == i.getName()) {
                return i;
            }
        }

        return null;
    }

    /**
     * Creates a node with given id and position (x,y)
     * @param id the node's id
     * @param x the x position
     * @param y the y position
     * @throws UnsupportedOperationException if node with the given id already exists
     */
    public Node createNode(int id, int x, int y) throws UnsupportedOperationException{
        if (this.findNode(id) != null) {
            throw new UnsupportedOperationException();
        }

        Node node = new Node(id, x, y);

        this.nodes.add(node);

        return node;
    }

    /**
     * Creates a street with a given name if not already existing, and returns it
     * @param name the street's name
     * @return the street that has the given name
     */
    public Street createStreet(String name) {
        Street street = this.findStreet(name);

        if (street != null) {
            return street;
        }

        street = new Street(name);

        this.streets.add(street);

        return street;
    }
}
