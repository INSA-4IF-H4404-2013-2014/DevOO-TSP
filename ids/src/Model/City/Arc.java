package Model.City;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public class Arc {

    /** the arc's street */
    private Street street;

    /** the arc's leaving node */
    private Node from;

    /** the arc's destination node */
    private Node to;

    /** the arc's length */
    private float length;

    /** the arc's speed */
    private float speed;

    /**
     * Constructor
     * @param street the street's name
     * @param from the node being leaved
     * @param to the destination node
     * @param length the arc's length
     * @param speed the arc's speed
     */
    protected Arc(Street street, Node from, Node to, float length, float speed) {
        street = street;
        from = from;
        to = to;

        // TODO: DO WE FULLY TRUST THE LENGTH PARAMETER ????
        // QUESTION FOR SOLNON : SHALL WE CHECK THE LENGTH IS POSSIBLE ACCORDING TO THE NODE COORDINATES ?

        street.getArcs().add(this);
        from.getOutgoing().add(this);
    }

    /**
     * Gets the street
     * @return the street
     */
    public Street getStreet() {
        return street;
    }

    /**
     * Gets the node being leaved
     * @return the node behing leaved
     */
    public Node getFrom() {
        return from;
    }

    /**
     * Gets the destination node
     * @return the destination node
     */
    public Node getTo() {
        return to;
    }

    /**
     * Gets the arc's length
     * @return the arc's length
     */
    public float getLength() {
        return length;
    }

    /**
     * Gets the arc's speed
     * @return the arc's speed
     */
    public float getSpeed() {
        return speed;
    }
}
