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

    public Arc(MapPanel mapPanel, Model.City.Arc modelArc) {
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

    public Model.City.Arc getModelArcFrom1To2() {
        return modelArcFrom1To2;
    }

    public Model.City.Arc getModelArcFrom2To1() {
        return modelArcFrom2To1;
    }

}
