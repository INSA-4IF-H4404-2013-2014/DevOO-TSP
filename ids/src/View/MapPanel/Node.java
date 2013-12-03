package View.MapPanel;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 08:49
 * To change this template use File | Settings | File Templates.
 */
public class Node {

    /** the parent map panel */
    private MapPanel mapPanel;

    /** the associated model node */
    private Model.City.Node modelNode;

    /** view coordinates */
    private int x;
    private int y;

    /**
     * Constructor
     * @param mapPanel the parent map panel
     * @param modelNode the model node
     */
    protected Node(MapPanel mapPanel, Model.City.Node modelNode) {
        this.mapPanel = mapPanel;
        this.modelNode = modelNode;
    }

    /**
     * Gets associated model node
     * @return the associated model node
     */
    protected Model.City.Node getModelNode() {
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
        return x;
    }

    /**
     * Gets the y coordinate
     * @return the y coordinate
     */
    protected int getY() {
        return y;
    }

    /**
     * Sets the y coordinate
     */
    protected void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate
     */
    protected void setY(int y) {
        this.y = y;
    }
}
