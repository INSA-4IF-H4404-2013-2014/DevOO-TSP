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

    /** view's center pos in the model basis */
    private Point modelCenterPos;

    /** graph model's size in the model basis */
    private Dimension modelSize;

    /** view/model scale factor */
    private double modelViewScaleFactor;


    /**
     * Constructor
     * @param modelGraph the associated model graph
     */
    public MapPanel() {
        this.nodes = new HashMap<Integer,Node>();
        this.arcs = new HashMap<Integer,Map<Integer,Arc>>();
        this.modelCenterPos = new Point();
        this.modelSize = new Dimension();
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
    public void paintComponent(Graphics gInt) {
        Graphics2D g = (Graphics2D) gInt;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int xGlobalOffset = this.getWidth() / 2 - (int)(this.modelViewScaleFactor * (double)this.modelCenterPos.x);
        int yGlobalOffset = this.getHeight() / 2 - (int)(this.modelViewScaleFactor * (double)this.modelCenterPos.y);

        g.setColor(noneground);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if(this.modelGraph == null) {
            return;
        }

        {
            int viewGraphSizeWidth = (int)(modelViewScaleFactor * (double)this.modelSize.width) + 2 * borderPadding;
            int viewGraphSizeHeight = (int)(modelViewScaleFactor * (double)this.modelSize.height) + 2 * borderPadding;

            g.setColor(background);
            g.fillRect(xGlobalOffset - borderPadding, yGlobalOffset - borderPadding, viewGraphSizeWidth, viewGraphSizeHeight);
        }

        g.setColor(nodeColor);
        int nodeRadius = (int)(modelViewScaleFactor * (double)MapPanel.nodeModelRadius);
        int xNodeOffset = xGlobalOffset - nodeRadius / 2;
        int yNodeOffset = yGlobalOffset - nodeRadius / 2;

        for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()) {
            Node node = entry.getValue();

            int x = xNodeOffset + (int)(modelViewScaleFactor * (double)node.getX());
            int y = yNodeOffset + (int)(modelViewScaleFactor * (double)node.getY());

            g.fillOval(x, y, nodeRadius, nodeRadius);
        }

        for(Map.Entry<Integer, Map<Integer, Arc>> entryTree : this.arcs.entrySet()) {
            for(Map.Entry<Integer, Arc> entry : entryTree.getValue().entrySet()) {
                Arc arc = entry.getValue();
                Node node1 = arc.getNode1();
                Node node2 = arc.getNode2();

                int x1 = xGlobalOffset + (int)(modelViewScaleFactor * (double)node1.getX());
                int y1 = yGlobalOffset + (int)(modelViewScaleFactor * (double)node1.getY());

                int x2 = xGlobalOffset + (int)(modelViewScaleFactor * (double)node2.getX());
                int y2 = yGlobalOffset + (int)(modelViewScaleFactor * (double)node2.getY());

                int nx = x2 - x1;
                int ny = y2 - y1;
                double angle = Math.atan2((double)ny, (double)nx);
                double nl = Math.sqrt((double)(nx * nx + ny * ny));

                g.translate(x1, y1);
                g.rotate(angle);
                g.fillRect(0, -MapPanel.arcModelThickness, (int)nl, 2 * MapPanel.arcModelThickness);
                g.rotate(-angle);
                g.translate(-x1, -y1);
            }
        }
    }

    /**
     * Updates viewport scale
     */
    public void fitToView() {
        double graphWidth = (double)(this.modelSize.width);
        double graphHeight = (double)(this.modelSize.height);

        double panelWidth = (double)(this.getWidth() - 2 * borderPadding);
        double panelHeight = (double)(this.getHeight() - 2 * borderPadding);

        double panelAspectRatio = panelWidth / panelHeight;
        double graphAspectRatio = graphWidth / graphHeight;

        if (panelAspectRatio > graphAspectRatio) {
            this.modelViewScaleFactor = panelHeight / graphHeight;
        }
        else {
            this.modelViewScaleFactor = panelWidth / graphWidth;
        }

        this.modelCenterPos.x = this.modelSize.width / 2;
        this.modelCenterPos.y = this.modelSize.height / 2;
    }

    /**
     * Builds view from the model graph
     */
    private void buildView() {
        int xModelMin = 0x7FFFFFFF;
        int yModelMin = 0x7FFFFFFF;
        int xModelMax = -0x7FFFFFFF;
        int yModelMax = -0x7FFFFFFF;

        for(Map.Entry<Integer,Model.City.Node> entry : modelGraph.getNodes().entrySet()) {
            Model.City.Node modelNode = entry.getValue();

            int x = modelNode.getX();
            int y = modelNode.getY();

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

        this.modelSize.width = xModelMax - xModelMin;
        this.modelSize.height = yModelMax - yModelMin;

        for(Map.Entry<Integer,Model.City.Node> entry : modelGraph.getNodes().entrySet()) {
            Model.City.Node modelNode = entry.getValue();

            int x = modelNode.getX() - xModelMin;
            int y = this.modelSize.height - (modelNode.getY() - yModelMin);

            Node node = new Node(this, modelNode, x, y);

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


    /** border padding in the view basis */
    private static int borderPadding = 50;

    /** none ground color */
    private static Color noneground = new Color(70, 70, 70);

    /** background color */
    private static Color background = new Color(255, 255, 255);

    /** node radius in the model's basis */
    private static int nodeModelRadius = 12;

    /** node radius in the graph */
    private static Color nodeColor = new Color(255, 160, 80);

    /** arc width in the model's basis */
    private static int arcModelThickness = 3;
}
