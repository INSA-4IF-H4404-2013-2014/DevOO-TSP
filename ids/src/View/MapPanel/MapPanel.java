package View.MapPanel;

import Model.City.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

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

    /** viewport's center pos in the model */
    private Point viewportOriginPos;

    /** model graph size */
    Dimension modelGraphSizes;


    /** viewport's scale factor */
    private double viewportScaleFactor;

    private static int borderPadding = 50;
    private static Color noneground = new Color(30, 30, 30);
    private static Color background = new Color(255, 255, 255);
    private static Color node_color = new Color(255, 160, 80);

    /**
     * Constructor
     * @param modelGraph the associated model graph
     */
    public MapPanel() {
        this.nodes = new HashMap<Integer,Node>();
        this.arcs = new HashMap<Integer,Map<Integer,Arc>>();
        this.viewportOriginPos = new Point();
        this.modelGraphSizes = new Dimension();
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
        int viewGraphSizeWidth = (int)(viewportScaleFactor * (double)this.modelGraphSizes.width) + 2 * borderPadding;
        int viewGraphSizeHeight = (int)(viewportScaleFactor * (double)this.modelGraphSizes.height) + 2 * borderPadding;

        int xOffset = viewportOriginPos.x + (this.getWidth() - viewGraphSizeWidth) / 2;
        int yOffset = viewportOriginPos.y + (this.getHeight() - viewGraphSizeHeight) / 2;

        g.setColor(noneground);
        g.fillRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());

        g.setColor(background);
        g.fillRect(xOffset, yOffset, viewGraphSizeWidth, viewGraphSizeHeight);

        g.setColor(node_color);
        xOffset += borderPadding;
        yOffset += borderPadding;

        for(Map.Entry<Integer, Node> entry : this.nodes.entrySet())
        {
            Node node = entry.getValue();

            g.fillOval(xOffset + node.getX(), yOffset + node.getY(), 5, 5);
        }

    }

    /**
     * Updates viewport scale
     */
    public void fitToView() {
        int xModelMin = 0x7FFFFFFF;
        int yModelMin = 0x7FFFFFFF;
        int xModelMax = -0x7FFFFFFF;
        int yModelMax = -0x7FFFFFFF;

        for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()) {
            Model.City.Node node = entry.getValue().getModelNode();

            int x = node.getX();
            int y = node.getY();

            if (x < xModelMin) {
                xModelMin = x;
            }
            if (y < yModelMin) {
                yModelMin = y;
            }
            if (x > xModelMax) {
                xModelMax = x;
            }
            if (y > yModelMax) {
                yModelMax = y;
            }
        }

        this.modelGraphSizes.width = xModelMax - xModelMin;
        this.modelGraphSizes.height = yModelMax - yModelMin;

        double graphWidth = xModelMax - xModelMin + 2 * borderPadding;
        double graphHeight = yModelMax - yModelMin + 2 * borderPadding;

        double panelAspectRatio = (double)this.getWidth() / (double)this.getHeight();
        double graphAspectRatio = graphWidth / graphHeight;

        if (panelAspectRatio > graphAspectRatio) {
            this.viewportScaleFactor = (double)this.getHeight() / graphHeight;
        }
        else {
            this.viewportScaleFactor = (double)this.getWidth() / graphWidth;
        }

        this.viewportOriginPos.setLocation(0, 0);

        this.actualizeNodesCoordinates();
    }

    /**
     * Actualizes nodes' coordinates
     */
    private void actualizeNodesCoordinates() {
        int xModelMin = 0x7FFFFFFF;
        int yModelMin = 0x7FFFFFFF;

        for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()) {
            Model.City.Node node = entry.getValue().getModelNode();

            int x = node.getX();
            int y = node.getY();

            if (x < xModelMin) {
                xModelMin = x;
            }
            if (y < yModelMin) {
                yModelMin = y;
            }
        }

        for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()) {
            Node node = entry.getValue();

            int x = node.getModelNode().getX() - xModelMin;
            int y = node.getModelNode().getY() - yModelMin;

            double xRelative = this.viewportScaleFactor * (double) x;
            double yRelative = this.viewportScaleFactor * (double) y;

            node.setX((int) xRelative);
            node.setY((int) yRelative);
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

        this.fitToView();
    }
}
