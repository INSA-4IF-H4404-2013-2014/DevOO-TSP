package View.MapPanel;

import Model.City.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.HashMap;

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
    protected Point modelCenterPos;

    /** graph model's size in the model basis */
    protected Dimension modelSize;

    /** view/model scale factor */
    protected double modelViewScaleFactor;

    /** save if the map is actually fitted to the panel or not */
    private boolean fittedScaleFactor;


    /**
     * Constructor
     * @param modelGraph the associated model graph
     */
    public MapPanel() {
        this.nodes = new HashMap<Integer,Node>();
        this.arcs = new HashMap<Integer,Map<Integer,Arc>>();
        this.modelCenterPos = new Point();
        this.modelSize = new Dimension();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                MapPanel panel = (MapPanel) event.getComponent();

                double smallestScaleFactor = panel.smallestScaleFactor();

                if (panel.modelViewScaleFactor < smallestScaleFactor || panel.fittedScaleFactor) {
                    fitToView();
                }
            }
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                MapPanel panel = (MapPanel) event.getComponent();

                double multiplier = Math.pow(0.5, (double) event.getWheelRotation());

                panel.multiplyScaleFactor(multiplier, event.getX(), event.getY());
            }
        });
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
     * Gets smallest scale factor
     */
    public double smallestScaleFactor() {
        double graphWidth = (double)(this.modelSize.width);
        double graphHeight = (double)(this.modelSize.height);

        double panelWidth = (double)(this.getWidth() - 2 * RenderContext.borderPadding);
        double panelHeight = (double)(this.getHeight() - 2 * RenderContext.borderPadding);

        double panelAspectRatio = panelWidth / panelHeight;
        double graphAspectRatio = graphWidth / graphHeight;

        if (panelAspectRatio > graphAspectRatio) {
            return panelHeight / graphHeight;
        }

        return panelWidth / graphWidth;
    }

    /**
     * Multiplies the model/view scale factor
     * @param multiplier the model/view scale factor multiplier
     * @param x the X coordinate in the view of the non moving point
     * @param y the Y coordinate in the view of the non moving point
     */
    public void multiplyScaleFactor(double multiplier, int x, int y) {
        double scaleFactor = this.modelViewScaleFactor * multiplier;
        double smallestScaleFactor = this.smallestScaleFactor();

        if (scaleFactor <= smallestScaleFactor) {
            fitToView();
            return;
        }

        scaleFactor = Math.min(scaleFactor, maxScaleFactor);
        multiplier = scaleFactor / this.modelViewScaleFactor;

        double xModelMoveVector = (double)(x - this.getWidth() / 2) / this.modelViewScaleFactor;
        double yModelMoveVector = (double)(y - this.getHeight() / 2) / this.modelViewScaleFactor;

        modelCenterPos.x += (int)(xModelMoveVector * (1.0 - 1.0 / multiplier));
        modelCenterPos.y += (int)(yModelMoveVector * (1.0 - 1.0 / multiplier));

        modelCenterPos.x = Math.min(Math.max(modelCenterPos.x, 0), modelSize.width);
        modelCenterPos.y = Math.min(Math.max(modelCenterPos.y, 0), modelSize.height);

        this.modelViewScaleFactor = scaleFactor;
        this.fittedScaleFactor = false;

        this.repaint();
    }

    /**
     * Fit the entire map in the available view.
     */
    public void fitToView() {
        this.modelViewScaleFactor = this.smallestScaleFactor();
        this.modelCenterPos.x = this.modelSize.width / 2;
        this.modelCenterPos.y = this.modelSize.height / 2;
        this.fittedScaleFactor = true;

        this.repaint();
    }

    /**
     * Paints map panel event
     * @param g the graphic context
     */
    @Override
    public void paintComponent(Graphics g) {
        RenderContext renderContext = new RenderContext((Graphics2D) g, this);

        renderContext.drawNoneground();

        if(this.modelGraph == null) {
            return;
        }

        renderContext.drawBackground();

        for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()) {
            renderContext.drawNode(entry.getValue());
        }

        for(Map.Entry<Integer, Map<Integer, Arc>> entryTree : this.arcs.entrySet()) {
            for(Map.Entry<Integer, Arc> entry : entryTree.getValue().entrySet()) {
                renderContext.drawArc(entry.getValue());
            }
        }

        renderContext.drawScale();
        renderContext.drawNorthArrow();

        if (fittedScaleFactor == false) {
            renderContext.drawGlobalView();
        }
    }

    /**
     * Gets model coordinate
     */
    protected int modelCoordinateX(int x) {
        return (int)((double)(x - this.getWidth() / 2) / modelViewScaleFactor + modelCenterPos.x);
    }

    protected int modelCoordinateY(int y) {
        return (int)((double)(y - this.getHeight() / 2) / modelViewScaleFactor + modelCenterPos.y);
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


    /** maximum scale factor */
    private static final double maxScaleFactor = 8.0;
}
