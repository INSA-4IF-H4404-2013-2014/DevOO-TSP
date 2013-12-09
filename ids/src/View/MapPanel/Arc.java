package View.MapPanel;

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
    }

    /**
     * Gets the model arc from node 1 to 2
     * @return
     */
    protected Model.City.Arc getModelArcFrom1To2() {
        return modelArcFrom1To2;
    }

    /**
     * gets the model arc from node 2 to 1
     * @return
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

}
