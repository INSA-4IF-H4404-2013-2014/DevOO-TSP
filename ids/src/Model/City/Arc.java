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

    /** the arc's length (in meters) */
    private float length;

    /** the arc's speed (in meters per seconds) */
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
        this.street = street;
        this.from = from;
        this.to = to;
        this.length = length;
        this.speed = speed;

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

    /**
     * Get the arc's cost in seconds
     * @return the arc's cost
     */
    public int getCost() {
        return (int) (length / speed);
    }

    /**
     * Give the type of direction from an arc to another arc.
     */
    public enum Direction {
        GO_ON, KEEP_GOING, TURN_LEFT, TURN_RIGHT, TURN_BACK
    }

    /**
     * Gets direction from this arc to the next given arc
     * @param arc the next arc you are going
     * @return
     *  - Direction.TURN_BACK if arc is going backward
     *  - Direction.KEEP_GOING if arc on the same street but not going backward
     *  - Direction.GO_ON if arc
     * @asserts
     *  - arc in this.to.getOutgoing()
     */
    public Direction getDirectionTo(Arc arc) {
        if(arc.getTo() == from) {
            return Direction.TURN_BACK;
        }

        String streetName = street.getName();
        String newStreetName = street.getName();

        if(newStreetName.equals(streetName)) {
            return Direction.KEEP_GOING;
        }

        int newStreetArcs = 0;

        for(Arc potentialNextArc : to.getOutgoing()) {
            if(potentialNextArc.getStreet().getName().equals(newStreetName)) {
                newStreetArcs += 1;
            }
        }

        if(newStreetArcs == 1) {
            return Direction.GO_ON;
        }

        return Direction.GO_ON;
    }
}
