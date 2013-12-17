package View.MapPanel;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 08:49
 * To change this template use File | Settings | File Templates.
 */
public class Arc {

    /**
     * An arc is between two node: node1 and node2.
     *
     * Contention: node1.id < node2.id
     */

    /** the model arc going from node1 to node2 */
    private Model.City.Arc modelArcFrom1To2;

    /** the model arc going from node2 to node1 */
    private Model.City.Arc modelArcFrom2To1;

    /** the parent map panel */
    private MapPanel mapPanel;

    /** itinerary colors from 1 to 2 */
    private LinkedList<Color> itineraryColorsFrom1To2;

    /** itinerary colors from 2 to 1 */
    private LinkedList<Color> itineraryColorsFrom2To1;

    /**
     * Constructor
     * @param mapPanel the map panel
     * @param modelArc the associated model arc
     */
    protected Arc(MapPanel mapPanel, Model.City.Arc modelArc) {
        this.mapPanel = mapPanel;

        Model.City.Node from = modelArc.getFrom();
        Model.City.Node to = modelArc.getTo();

        if (from.getId() < to.getId()) {
            modelArcFrom1To2 = modelArc;
            modelArcFrom2To1 = to.findOutgoingTo(from);
        }
        else {
            modelArcFrom2To1 = modelArc;
            modelArcFrom1To2 = to.findOutgoingTo(from);
        }

        itineraryColorsFrom1To2 = new LinkedList<Color>();
        itineraryColorsFrom2To1 = new LinkedList<Color>();
    }

    /**
     * Gets the model arc leaving node leavingNode
     * @param leavingNode the leaving node id (bust me be 1 or 2)
     * @return the model arc
     */
    protected Model.City.Arc getModelArcFrom(int leavingNode) {
        if (leavingNode == 2) {
            return modelArcFrom2To1;
        }
        return modelArcFrom1To2;
    }

    /**
     * Gets the model arc from node 1 to 2
     * @return the model arc
     */
    protected Model.City.Arc getModelArcFrom1To2() {
        return modelArcFrom1To2;
    }

    /**
     * gets the model arc from node 2 to 1
     * @return the model arc
     */
    protected Model.City.Arc getModelArcFrom2To1() {
        return modelArcFrom2To1;
    }

    /**
     * gets the model street
     * @return the model street
     */
    protected Model.City.Street getModelStreet() {
        if(modelArcFrom2To1 != null) {
            return modelArcFrom2To1.getStreet();
        }

        return modelArcFrom1To2.getStreet();
    }

    /**
     * Gets the node 1
     * @return the node 1
     */
    protected Node getNode1() {
        if (modelArcFrom1To2 != null) {
            return mapPanel.findNode(modelArcFrom1To2.getFrom().getId());
        }

        return mapPanel.findNode(modelArcFrom2To1.getTo().getId());
    }

    /**
     * Gets the node 2
     * @return the node 2
     */
    protected Node getNode2() {
        if (modelArcFrom2To1 != null) {
            return mapPanel.findNode(modelArcFrom2To1.getFrom().getId());
        }

        return mapPanel.findNode(modelArcFrom1To2.getTo().getId());
    }

    /**
     * Gets the itinerary colors from node <leavingNode> to node 3 - <leavingNode> is an itinerary
     * @param leavingNode the leaving node id (bust me be 1 or 2)
     * @return the itinerary colors
     */
    public LinkedList<Color> getItineraryColorsFrom(int leavingNode) {
        if(leavingNode == 2) {
            return itineraryColorsFrom2To1;
        }

        return itineraryColorsFrom1To2;
    }

    /**
     * Resets the itinerary colors
     */
    public void resetItineraryColors() {
        itineraryColorsFrom1To2.clear();
        itineraryColorsFrom2To1.clear();
    }
}
