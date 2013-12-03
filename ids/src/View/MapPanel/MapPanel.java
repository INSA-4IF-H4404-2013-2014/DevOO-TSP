package View.MapPanel;

import Model.City.Graph;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import java.awt.Color;
import java.awt.Graphics;

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


    private static Color background = new Color(255, 255, 255);
    private static Color node_color = new Color(255, 160, 80);

    /**
     * Constructor
     * @param modelGraph the associated model graph
     */
    public MapPanel() {
        this.nodes = new HashMap<Integer,Node>();
        this.arcs = new HashMap<Integer,Map<Integer,Arc>>();
    }

    /**
     * Sets the model
     * @param modelGraph the model we want to set
     */
    public void setModel(Graph modelGraph) {
        this.nodes.clear();
        this.arcs.clear();

        this.modelGraph = modelGraph;

        this.buildView();
        this.repaint();
    }

    /**
     * Get a view node from a given node's id
     * @param nodeId the given node's id
     * @return
     *  - null not found
     *  - the view node
     */
    public Node findNode(int nodeId) {
        return this.nodes.get(nodeId);
    }

    /**
     * Gets a view arc from two nodes' id
     * @param from a node id
     * @param to an other node id
     * @return
     *  - null if no arc exists between the given nodes
     *  - the arc if found
     */
    public Arc findArc(int from, int to) {
        if (to < from) {
            int temp = to;
            to = from;
            from = temp;
        }

        Map<Integer,Arc> tree = this.arcs.get(from);

        if (tree == null) {
            return null;
        }

        return tree.get(to);
    }

    /**
     * Paints map panel event
     * @param g the graphic context
     */
    @Override
    public void paintComponent(Graphics g) {
        setBackground(background);
        g.setColor(background);
        g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());

        g.setColor(node_color);

        for(Map.Entry<Integer, Node> entry : this.nodes.entrySet())
        {
            Model.City.Node modelNode = entry.getValue().getModelNode();

            g.fillOval(modelNode.getX(), modelNode.getY(), 5, 5);
        }

    }

    /**
     * Builds view from the model graph
     */
    private void buildView() {

        for(Map.Entry<Integer,Model.City.Node> entry : modelGraph.getNodes().entrySet()) {
            Node node = new Node(this, entry.getValue());

            this.nodes.put(entry.getValue().getId(), node);
        }

        for(Model.City.Arc modelArc : modelGraph.getArcs()) {
            int from = modelArc.getFrom().getId();
            int to = modelArc.getTo().getId();

            if (to < from) {
                int temp = to;
                to = from;
                from = temp;
            }

            if (this.findArc(from, to) != null) {
                continue;
            }

            Arc arc = new Arc(this, modelArc);

            Map<Integer,Arc> tree = this.arcs.get(from);

            if (this.arcs.get(from) == null) {
                tree = new HashMap<Integer,Arc>();
                this.arcs.put(from, tree);
            }

            tree.put(to, arc);
        }

    }
}
