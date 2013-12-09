package View.MapPanel;

import Model.ChocoSolver.CalculatedRound;
import Model.City.Network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private Network modelNetwork;

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

    /** selected node */
    protected Node selectedNode;

    /** node event listener */
    private NodeListener nodeEventListener;

    /**
     * Constructor
     */
    public MapPanel() {
        this.nodes = new HashMap<Integer,Node>();
        this.arcs = new HashMap<Integer,Map<Integer,Arc>>();
        this.modelCenterPos = new Point();
        this.modelSize = new Dimension();
        this.nodeEventListener = null;

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

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                MapPanel panel = (MapPanel) mouseEvent.getComponent();

                if(panel.nodeEventListener == null) {
                    return;
                }

                int x = panel.modelCoordinateX(mouseEvent.getX());
                int y = panel.modelCoordinateY(mouseEvent.getY());

                int minDistancePow = Math.max(panel.modelSize.width, panel.modelSize.height);
                minDistancePow *= minDistancePow;

                Model.City.Node nearestNode = null;

                for(Map.Entry<Integer,Node> entry : panel.nodes.entrySet()) {
                    Node node = entry.getValue();

                    int dx = node.getX() - x;
                    int dy = node.getY() - y;

                    int distancePow = dx * dx + dy * dy;

                    if(distancePow < minDistancePow) {
                        minDistancePow = distancePow;
                        nearestNode = node.getModelNode();
                    }
                }

                if(nearestNode == null) {
                    return;
                }

                if(minDistancePow >= RenderContext.streetNodeRadius * RenderContext.streetNodeRadius) {
                    return;
                }

                panel.nodeEventListener.nodeClicked(panel, nearestNode);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
    }

    /**
     * Sets the model
     * @param modelNetwork the model we want to set
     */
    public void setModel(Network modelNetwork) {
        nodes.clear();
        arcs.clear();

        this.modelNetwork = modelNetwork;

        this.buildView();
        this.repaint();
    }

    /**
     * Gets the currently selected node
     * @return the model selected node or null if any are selected
     */
    public Model.City.Node getSelectedNode() {
        return selectedNode.getModelNode();
    }

    /**
     * Sets the currently selected node. This assumes that selectedNode is in the current model
     */
    public void setSelectedNode(Model.City.Node node) {
        int nodeId = node.getId();

        selectedNode = this.findNode(nodeId);

        this.repaint();
    }

    /**
     * Gets the current node event listener
     * @return the current node event listener or null
     */
    public NodeListener getNodeEventListener() {
        return nodeEventListener;
    }

    /**
     * Sets the current node event listener
     * @param nodeEventListener the node event listener we want to set
     */
    public void setNodeEventListener(NodeListener nodeEventListener) {
        this.nodeEventListener = nodeEventListener;
    }

    /**
     * Gets smallest scale factor
     */
    public double smallestScaleFactor() {
        double graphWidth = (double)(modelSize.width);
        double graphHeight = (double)(modelSize.height);

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
        double scaleFactor = modelViewScaleFactor * multiplier;
        double smallestScaleFactor = this.smallestScaleFactor();

        if (scaleFactor <= smallestScaleFactor) {
            fitToView();
            return;
        }

        scaleFactor = Math.min(scaleFactor, maxScaleFactor);
        multiplier = scaleFactor / modelViewScaleFactor;

        double xModelMoveVector = (double)(x - this.getWidth() / 2) / modelViewScaleFactor;
        double yModelMoveVector = (double)(y - this.getHeight() / 2) / modelViewScaleFactor;

        modelCenterPos.x += (int)(xModelMoveVector * (1.0 - 1.0 / multiplier));
        modelCenterPos.y += (int)(yModelMoveVector * (1.0 - 1.0 / multiplier));

        modelCenterPos.x = Math.min(Math.max(modelCenterPos.x, 0), modelSize.width);
        modelCenterPos.y = Math.min(Math.max(modelCenterPos.y, 0), modelSize.height);

        modelViewScaleFactor = scaleFactor;
        fittedScaleFactor = false;

        this.repaint();
    }

    /**
     * Fit the entire map in the available view.
     */
    public void fitToView() {
        modelViewScaleFactor = this.smallestScaleFactor();
        modelCenterPos.x = modelSize.width / 2;
        modelCenterPos.y = modelSize.height / 2;
        fittedScaleFactor = true;

        this.repaint();
    }

    /**
     * Paints map panel event
     * @param g the graphic context
     */
    @Override
    public void paintComponent(Graphics g) {
        RenderContext renderContext = new RenderContext((Graphics2D) g, this);

        renderContext.drawBackground();

        if(modelNetwork == null) {
            renderContext.drawEmptyMessage();
            return;
        }

        renderContext.setTransformNetwork();

        for(Map.Entry<Integer, Map<Integer, Arc>> entryTree : arcs.entrySet()) {
            for(Map.Entry<Integer, Arc> entry : entryTree.getValue().entrySet()) {
                renderContext.drawArcStreetName(entry.getValue());
            }
        }

        for(Map.Entry<Integer, Map<Integer, Arc>> entryTree : arcs.entrySet()) {
            for(Map.Entry<Integer, Arc> entry : entryTree.getValue().entrySet()) {
                renderContext.drawArcBorders(entry.getValue());
            }
        }

        for(Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            renderContext.drawNodeBorders(entry.getValue());
        }

        for(Map.Entry<Integer, Map<Integer, Arc>> entryTree : arcs.entrySet()) {
            for(Map.Entry<Integer, Arc> entry : entryTree.getValue().entrySet()) {
                renderContext.drawArc(entry.getValue());
            }
        }

        for(Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            renderContext.drawNode(entry.getValue());
        }

        renderContext.setTransformIdentity();

        renderContext.drawScale();
        renderContext.drawNorthArrow();

        if (!fittedScaleFactor) {
            renderContext.drawGlobalView();
        }
    }


    public void setRound(CalculatedRound round) {
        //TODO: by Guillaume
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
     * Get a view node from a given node's id
     * @param nodeId the given node's id
     * @return
     *  - null not found
     *  - the view node
     */
    protected Node findNode(int nodeId) {
        return nodes.get(nodeId);
    }

    /**
     * Gets a view arc from two nodes' id
     * @param from a node id
     * @param to an other node id
     * @return
     *  - null if no arc exists between the given nodes
     *  - the arc if found
     */
    protected Arc findArc(int from, int to) {
        if (to < from) {
            int temp = to;
            to = from;
            from = temp;
        }

        Map<Integer,Arc> tree = arcs.get(from);

        if (tree == null) {
            return null;
        }

        return tree.get(to);
    }

    /**
     * Builds view from the model graph
     */
    private void buildView() {
        int xModelMin = 0x7FFFFFFF;
        int yModelMin = 0x7FFFFFFF;
        int xModelMax = -0x7FFFFFFF;
        int yModelMax = -0x7FFFFFFF;

        for(Map.Entry<Integer,Model.City.Node> entry : modelNetwork.getNodes().entrySet()) {
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

        modelSize.width = xModelMax - xModelMin;
        modelSize.height = yModelMax - yModelMin;

        for(Map.Entry<Integer,Model.City.Node> entry : modelNetwork.getNodes().entrySet()) {
            Model.City.Node modelNode = entry.getValue();

            int x = modelNode.getX() - xModelMin;
            int y = modelSize.height - (modelNode.getY() - yModelMin);

            Node node = new Node(this, modelNode, x, y);

            nodes.put(entry.getValue().getId(), node);
        }

        for(Model.City.Arc modelArc : modelNetwork.getArcs()) {
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

            Map<Integer,Arc> tree = arcs.get(from);

            if (arcs.get(from) == null) {
                tree = new HashMap<Integer,Arc>();
                arcs.put(from, tree);
            }

            tree.put(to, arc);
        }

        this.fitToView();
    }


    /** maximum scale factor */
    private static final double maxScaleFactor = 8.0;
}
