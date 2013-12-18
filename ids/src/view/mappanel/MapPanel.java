package view.mappanel;

import model.chocosolver.CalculatedRound;
import model.city.Network;
import model.delivery.Itinerary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
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
    private Network modelNetwork;

    /** the model round */
    CalculatedRound modelRound;

    /** nodes map */
    protected Map<Integer,NodeView> nodes;

    /** arcs map */
    protected Map<Integer,Map<Integer,ArcView>> arcs;

    /** view's center pos in the model basis */
    protected Point modelCenterPos;

    /** view's center pos in the model basis */
    protected Point modelMinPos;

    /** view's center pos in the model basis */
    protected Point modelMaxPos;

    /** view/model scale factor */
    protected double modelViewScaleFactor;

    /** save if the map is actually fitted to the panel or not */
    private boolean fittedScaleFactor;

    /** selected node */
    protected NodeView selectedNodeView;

    /** node event listener */
    private NodeListener nodeEventListener;

    /**
     * Constructor
     */
    public MapPanel() {
        this.nodes = new HashMap<Integer,NodeView>();
        this.arcs = new HashMap<Integer,Map<Integer,ArcView>>();
        this.modelCenterPos = new Point();
        this.modelMinPos = new Point();
        this.modelMaxPos = new Point();
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

                double multiplier = Math.pow(0.5, 0.5 * (double) event.getWheelRotation());

                panel.multiplyScaleFactor(multiplier, event.getX(), event.getY());
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                MapPanel panel = (MapPanel) mouseEvent.getComponent();

                if(mouseEvent.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                if (panel.nodeEventListener == null) {
                    return;
                }

                int x = panel.modelCoordinateX(mouseEvent.getX());
                int y = panel.modelCoordinateY(mouseEvent.getY());

                int minDistancePow = Math.max(modelMaxPos.x - modelMinPos.x, modelMaxPos.y - modelMinPos.y);
                minDistancePow *= minDistancePow;

                model.city.Node nearestNode = null;

                for (Map.Entry<Integer, NodeView> entry : panel.nodes.entrySet()) {
                    NodeView nodeView = entry.getValue();

                    int dx = nodeView.getX() - x;
                    int dy = nodeView.getY() - y;

                    int distancePow = dx * dx + dy * dy;

                    if (distancePow < minDistancePow) {
                        minDistancePow = distancePow;
                        nearestNode = nodeView.getModelNode();
                    }
                }

                if (nearestNode == null ||
                        minDistancePow >= RenderContext.streetNodeRadius * RenderContext.streetNodeRadius) {
                    panel.nodeEventListener.backgroundClicked(panel);
                    return;
                }

                panel.nodeEventListener.nodeClicked(panel, nearestNode);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                MapPanel panel = (MapPanel) mouseEvent.getComponent();

                if(!panel.fittedScaleFactor) {
                    panel.addMouseMotionListener(new MouseDragging(mouseEvent));
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                MapPanel panel = (MapPanel) mouseEvent.getComponent();

                for(MouseMotionListener e : panel.getMouseMotionListeners()) {
                    panel.removeMouseMotionListener(e);
                }
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
     * @param network the model we want to set
     */
    public void setModel(Network network) {
        modelNetwork = network;
        modelRound = null;
        selectedNodeView = null;

        this.refreshNodeAndArcs();
    }

    /**
     * Sets the calculated round to show
     * @param round the calculated round to show
     */
    public void setRound(CalculatedRound round) {
        modelRound = round;

        this.refreshNodesDeliveries();
        this.refreshArcsItineraries();
        this.repaint();
    }

    /**
     * Gets the currently selected node
     * @return the model selected node or null if any are selected
     */
    public model.city.Node getSelectedNodeView() {
        if(selectedNodeView == null) {
            return null;
        }

        return selectedNodeView.getModelNode();
    }

    /**
     * Sets the currently selected node. This assumes that selectedNodeView is in the current model
     */
    public void setSelectedNodeView(model.city.Node node) {
        if(node == null) {
            selectedNodeView = null;
        } else {
            int nodeId = node.getId();
            selectedNodeView = this.findNode(nodeId);
        }

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
        double graphWidth = (double)(getModelDimension().width);
        double graphHeight = (double)(getModelDimension().height);

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

        if (scaleFactor <= smallestScaleFactor || smallestScaleFactor > maxScaleFactor) {
            fitToView();
            return;
        }

        scaleFactor = Math.min(scaleFactor, maxScaleFactor);
        multiplier = scaleFactor / modelViewScaleFactor;

        double xModelMoveVector = (double)(x - this.getWidth() / 2) / modelViewScaleFactor;
        double yModelMoveVector = (double)(this.getHeight() / 2 - y) / modelViewScaleFactor;

        modelCenterPos.x += (int)(xModelMoveVector * (1.0 - 1.0 / multiplier));
        modelCenterPos.y += (int)(yModelMoveVector * (1.0 - 1.0 / multiplier));

        modelCenterPos.x = Math.min(Math.max(modelCenterPos.x, modelMinPos.x), modelMaxPos.x);
        modelCenterPos.y = Math.min(Math.max(modelCenterPos.y, modelMinPos.y), modelMaxPos.y);

        modelViewScaleFactor = scaleFactor;
        fittedScaleFactor = false;

        this.repaint();
    }

    /**
     * Fit the entire map in the available view.
     */
    public void fitToView() {
        modelViewScaleFactor = Math.min(this.smallestScaleFactor(), maxScaleFactor);
        modelCenterPos.x = (modelMinPos.x + modelMaxPos.x) / 2;
        modelCenterPos.y = (modelMinPos.y + modelMaxPos.y) / 2;
        fittedScaleFactor = true;

        this.repaint();
    }

    /**
     * Test if the network view fully fits
     * @return true if the network view fully fits
     */
    public boolean isViewFitted() {
        return fittedScaleFactor;
    }

    /**
     * Center the view on a model node
     * @param node the node we want to center on
     */
    public void centerOn(model.city.Node node) {
        centerOn(node, getModelViewScaleFactor());
    }

    /**
     * Center the view on a model node
     * @param node the node we want to center on
     * @param scaleFactor the scale factor we want to apply
     */
    public void centerOn(model.city.Node node, double scaleFactor) {
        double smallestScaleFactor = smallestScaleFactor();

        if (scaleFactor <= smallestScaleFactor || smallestScaleFactor > maxScaleFactor) {
            fitToView();
            return;
        }

        modelViewScaleFactor = Math.min(scaleFactor, maxScaleFactor);
        fittedScaleFactor = false;

        setModelCenterPos(node.getX(), node.getY());

        repaint();
    }

    /**
     * tests if the node is visible on the view
     * @param node the node we want to test
     * @return true if node is visible, false elsewhere
     */
    public boolean isVisible(model.city.Node node) {
        int x = viewCoordinateX(node.getX());
        int y = viewCoordinateY(node.getY());

        return (x >= 0 && x < getWidth() && y >= 0 && y < getHeight());
    }

    /**
     * Gets the model/view scale factor
     * @return the model/view scale factor (view = modelViewScaleFactor * model)
     */
    public double getModelViewScaleFactor() {
        return modelViewScaleFactor;
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
            renderContext.drawPanelBorders();
            return;
        }

        renderContext.setTransformNetwork();
        {
            renderContext.drawStreetNames();
            renderContext.drawBorders();
            renderContext.drawStreets();
        }
        renderContext.setTransformIdentity();

        renderContext.drawScale();
        renderContext.drawNorthArrow();

        if (!fittedScaleFactor) {
            renderContext.drawGlobalView();
        }

        renderContext.drawPanelBorders();
    }

    /**
     * Gets model X coordinate
     */
    protected int modelCoordinateX(int x) {
        return (int)((double)(x - this.getWidth() / 2) / modelViewScaleFactor + modelCenterPos.x);
    }

    /**
     * Gets model Y coordinate
     */
    protected int modelCoordinateY(int y) {
        return (int)(modelCenterPos.y - (double)(y - this.getHeight() / 2) / modelViewScaleFactor);
    }

    /**
     * Gets view X coordinate
     */
    protected int viewCoordinateX(int x) {
        return (int)((double)(x - modelCenterPos.x) * modelViewScaleFactor + 0.5 * (double) this.getWidth());
    }

    /**
     * Gets view Y coordinate
     */
    protected int viewCoordinateY(int y) {
        return (int)(0.5 * (double)this.getHeight() - (double)(y - modelCenterPos.y) * modelViewScaleFactor);
    }

    /**
     * Get a view node from a given node's id
     * @param nodeId the given node's id
     * @return
     *  - null not found
     *  - the view node
     */
    protected NodeView findNode(int nodeId) {
        return nodes.get(nodeId);
    }

    /**
     * Gets a view arcView from two nodes' id
     * @param from a node id
     * @param to an other node id
     * @return
     *  - null if no arcView exists between the given nodes
     *  - the arcView if found
     */
    protected ArcView findArc(int from, int to) {
        if (to < from) {
            int temp = to;
            to = from;
            from = temp;
        }

        Map<Integer,ArcView> tree = arcs.get(from);

        if (tree == null) {
            return null;
        }

        return tree.get(to);
    }

    /**
     * Gets model's dimension
     * @return the model dimension (modelMaxPos - modelMinPos)
     */
    protected Dimension getModelDimension() {
        return new Dimension(modelMaxPos.x - modelMinPos.x, modelMaxPos.y - modelMinPos.y);
    }

    /**
     * refreshes model's min/max coordinates
     */
    private void refreshModelMinMax() {
        modelMinPos.x = 0x7FFFFFFF;
        modelMinPos.y = 0x7FFFFFFF;
        modelMaxPos.x = -0x7FFFFFFF;
        modelMaxPos.y = -0x7FFFFFFF;

        for(Map.Entry<Integer, model.city.Node> entry : modelNetwork.getNodes().entrySet()) {
            model.city.Node modelNode = entry.getValue();

            int x = modelNode.getX();
            int y = modelNode.getY();

            modelMinPos.x = Math.min(modelMinPos.x, x);
            modelMinPos.y = Math.min(modelMinPos.y, y);
            modelMaxPos.x = Math.max(modelMaxPos.x, x);
            modelMaxPos.y = Math.max(modelMaxPos.y, y);
        }
    }

    /**
     * Refreshes view nodes/arcs from the model
     */
    private void refreshNodeAndArcs() {
        nodes.clear();
        arcs.clear();

        if(modelNetwork == null) {
            return;
        }

        refreshModelMinMax();

        for(Map.Entry<Integer, model.city.Node> entry : modelNetwork.getNodes().entrySet()) {
            model.city.Node modelNode = entry.getValue();

            NodeView nodeView = new NodeView(this, modelNode);

            nodes.put(entry.getValue().getId(), nodeView);
        }

        for(model.city.Arc modelArc : modelNetwork.getArcs()) {
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

            ArcView arcView = new ArcView(this, modelArc);

            Map<Integer,ArcView> tree = arcs.get(from);

            if (arcs.get(from) == null) {
                tree = new HashMap<Integer,ArcView>();
                arcs.put(from, tree);
            }

            tree.put(to, arcView);
        }

        this.fitToView();
    }

    /**
     * Refreshes view's nodes' deliveries from the calculated round
     */
    private void refreshNodesDeliveries() {
        for(Map.Entry<Integer, NodeView> entry : nodes.entrySet()) {
            entry.getValue().setColor(RenderContext.streetBorderColor);
            entry.getValue().setDeliveryDelayed(false);
        }

        if(modelRound == null) {
            return;
        }

        List<Integer> deliveryNodesId = modelRound.getOrderedNodesId();

        if(deliveryNodesId.size() <= 1) {
            deliveryNodesId = modelRound.getNodesId();
        }

        int colorId = RenderContext.itineraryColors.length - 1;

        for(int deliveryNodeId : deliveryNodesId) {
            NodeView nodeView = findNode(deliveryNodeId);

            nodeView.setColor(RenderContext.itineraryColors[colorId]);

            if(modelRound.isDelayed(deliveryNodeId)) {
                nodeView.setDeliveryDelayed(true);
            }

            colorId++;

            if(colorId == RenderContext.itineraryColors.length) {
                colorId = 0;
            }
        }

        findNode(modelRound.getWarehouse().getId()).setColor(RenderContext.itineraryWarehouseColor);
    }

    /**
     * Refreshes view's arcs' itineraries from the calculated round
     */
    private void refreshArcsItineraries() {
        for(Map.Entry<Integer, Map<Integer, ArcView>> entryTree : arcs.entrySet()) {
            for(Map.Entry<Integer, ArcView> entry : entryTree.getValue().entrySet()) {
                entry.getValue().resetItineraryColors();
            }
        }

        if(modelRound == null) {
            return;
        }

        int colorId = 0;

        for(Itinerary modelItinerary : modelRound.getOrderedItineraries()) {
            for(model.city.Arc modelArc : modelItinerary.getArcs()) {
                int from = modelArc.getFrom().getId();
                int to = modelArc.getTo().getId();

                ArcView arcView = findArc(from, to);

                if (arcView.getNode1().getModelNode() == modelArc.getFrom()) {
                    arcView.getItineraryColorsFrom(1).add(RenderContext.itineraryColors[colorId]);
                } else {
                    arcView.getItineraryColorsFrom(2).add(RenderContext.itineraryColors[colorId]);
                }
            }

            colorId++;

            if(colorId == RenderContext.itineraryColors.length) {
                colorId = 0;
            }
        }
    }

    /**
     * Sets the new model center position
     * @param x the new model center X position
     * @param y the new model center Y position
     * @caution it doesn't redraw the view
     */
    public void setModelCenterPos(int x, int y) {
        modelCenterPos.x = Math.min(Math.max(x, modelMinPos.x), modelMaxPos.x);
        modelCenterPos.y = Math.min(Math.max(y, modelMinPos.y), modelMaxPos.y);
    }

    /**
     * mapPanel's mouse dragging class instantiated to drag the map panel view
     */
    private class MouseDragging implements MouseMotionListener {

        /** cursor start X position */
        private int startX;

        /** cursor start Y position */
        private int startY;

        /** previous center X position */
        private int previousModelCenterPosX;

        /** previous center Y position */
        private int previousModelCenterPosY;

        /**
         * Constructor
         * @param mouseEvent the mouseEvent
         */
        public MouseDragging(MouseEvent mouseEvent) {
            MapPanel panel = (MapPanel) mouseEvent.getComponent();

            startX = mouseEvent.getX();
            startY = mouseEvent.getY();
            previousModelCenterPosX = panel.modelCenterPos.x;
            previousModelCenterPosY = panel.modelCenterPos.y;
        }

        /**
         * overrides dragging mouse event
         * @param mouseEvent the mouse event
         */
        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            MapPanel panel = (MapPanel) mouseEvent.getComponent();

            double modelMoveVectorX = (double)(startX - mouseEvent.getX()) / modelViewScaleFactor;
            double modelMoveVectorY = (double)(mouseEvent.getY() - startY) / modelViewScaleFactor;

            int modelCenterPosX = previousModelCenterPosX + (int)modelMoveVectorX;
            int modelCenterPosY = previousModelCenterPosY + (int)modelMoveVectorY;

            panel.setModelCenterPos(modelCenterPosX, modelCenterPosY);
            panel.repaint();
        }

        /**
         * overrides motion mouse event
         * @param mouseEvent the mouse event
         */
        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
        }
    }


    /** maximum scale factor */
    private static final double maxScaleFactor = 8.0;
}
