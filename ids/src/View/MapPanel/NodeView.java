package view.MapPanel;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 08:49
 * To change this template use File | Settings | File Templates.
 */
public class NodeView {

    /** the parent map panel */
    private MapPanel mapPanel;

    /** the associated model node */
    private model.City.Node modelNode;

    /** the node's color */
    private Color color;

    /** is the node's delivery delayed */
    private boolean deliveryDelayed;


    /**
     * Constructor
     * @param mapPanel the parent map panel
     * @param modelNode the model node
     */
    protected NodeView(MapPanel mapPanel, model.City.Node modelNode) {
        this.mapPanel = mapPanel;
        this.modelNode = modelNode;
        this.color = RenderContext.streetBorderColor;
        this.deliveryDelayed = false;
    }

    /**
     * Gets associated model node
     * @return the associated model node
     */
    protected model.City.Node getModelNode() {
        return modelNode;
    }

    /**
     * Gets the parent map panel
     * @return the parent map panel
     */
    protected MapPanel getMapPanel() {
        return mapPanel;
    }

    /**
     * Gets the x coordinate
     * @return the x coordinate
     */
    protected int getX() {
        return modelNode.getX();
    }

    /**
     * Gets the y coordinate
     * @return the y coordinate
     */
    protected int getY() {
        return modelNode.getY();
    }

    /**
     * Gets the node's color
     * @return the node's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * tests if the delivery is delayed
     * @return true if the delivery is delayed
     */
    public boolean isDeliveryDelayed() {
        return deliveryDelayed;
    }

    /**
     * Sets the node's color
     * @param color the new node's color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the node's delivery late
     * @param deliveryDelayed the new delayed boolean to set
     */
    public void setDeliveryDelayed(boolean deliveryDelayed) {
        this.deliveryDelayed = deliveryDelayed;
    }
}
