package View.MapPanel;

import Model.City.Graph;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 08:50
 * To change this template use File | Settings | File Templates.
 */
public class MapPanel extends JPanel {

    /** associated model graph */
    private Graph modelGraph;

    /** nodes map */
    private Map<Integer,Node> nodes;

    /** arcs map */
    private Map<Integer,Map<Integer,Arc>> arcs;

    /**
     * Constructor
     * @param modelGraph the associated model graph
     */
    public MapPanel(Graph modelGraph) {
        this.modelGraph = modelGraph;
        this.nodes = new HashMap<Integer,Node>();
        this.arcs = new HashMap<Integer,Map<Integer,Arc>>();
    }

}
