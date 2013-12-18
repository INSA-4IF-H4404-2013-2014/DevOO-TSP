package model.City;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public class Arc {

    /** the arcView's street */
    private Street street;

    /** the arcView's leaving node */
    private Node from;

    /** the arcView's destination node */
    private Node to;

    /** the arcView's length (in meters) */
    private float length;

    /** the arcView's speed (in meters per seconds) */
    private float speed;

    /**
     * Constructor
     * @param street the street's name
     * @param from the node being leaved
     * @param to the destination node
     * @param length the arcView's length
     * @param speed the arcView's speed
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
     * Gets the arcView's length
     * @return the arcView's length
     */
    public float getLength() {
        return length;
    }

    /**
     * Gets the arcView's speed
     * @return the arcView's speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Get the arcView's cost in seconds
     * @return the arcView's cost
     */
    public int getCost() {
        return (int) (length / speed);
    }

    /**
     * Gets arcView's direction angle
     * @return the arcView's orientation angle
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
     * Give the type of direction from an arcView to another arcView.
     */
    public enum Direction {
        GO_ON, KEEP_GOING, TURN_LEFT, TURN_RIGHT, TURN_BACK
    }

    /**
     * Gets direction from this arcView to the next given arcView
     * @param arc the next arcView you are going
     * @return
     *  - Direction.TURN_BACK if arcView is going backward
     *  - Direction.KEEP_GOING if arcView on the same street but not going backward
     *  - Direction.GO_ON if arcView
     * @asserts
     *  - arcView in this.to.getOutgoing()
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
