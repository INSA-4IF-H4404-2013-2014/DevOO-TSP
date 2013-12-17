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
        to.getIncoming().add(this);
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
     * Gets arc's direction angle
     * @return the arc's orientation angle
     *  - 0.0 is indicating the north
     *  - 90 is indicating the est
     *  - 180 is indicating the south
     *  - 270 is indicating the west
     */
    public double getDirectionAngle() {
        double radianAngle = Math.atan2(to.getX() - from.getX(),to.getY() - from.getY());

        if(radianAngle < 0.0) {
            radianAngle += 2.0 * Math.PI;
        }

        return radianAngle * 180.0 / Math.PI;
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

        String newStreetName = arc.getStreet().getName();

        if(street.getName().equals(newStreetName)) {
            return Direction.KEEP_GOING;
        }

        int newStreetArcs = 0;
        Arc otherNewStreetArc = null;

        for(Arc potentialNextArc : to.getOutgoing()) {
            if(potentialNextArc.getStreet().getName().equals(newStreetName)) {
                newStreetArcs += 1;

                if(!arc.getFrom().equals(potentialNextArc.getTo())) {
                    otherNewStreetArc = potentialNextArc;
                }
            }
        }

        if(newStreetArcs != 2) {
            /**
             * If newStreetArcs > 2, we don't now what to do.
             * If newStreetArcs = 1, it is obvious
             */
            return Direction.GO_ON;
        }

        double rotatingAngle = (arc.getDirectionAngle() - getDirectionAngle());

        if(rotatingAngle < 0.0) {
            rotatingAngle += 360.0;
        }

        if (rotatingAngle < 180.0) {
            return Direction.TURN_RIGHT;
        }

        return Direction.TURN_LEFT;
    }
}
