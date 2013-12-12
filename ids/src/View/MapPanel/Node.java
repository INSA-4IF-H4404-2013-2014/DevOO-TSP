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

    /** the node's kind */
    private Kind kind;


    /**
     * Constructor
     * @param mapPanel the parent map panel
     * @param modelNode the model node
     */
    protected Node(MapPanel mapPanel, Model.City.Node modelNode) {
        this.mapPanel = mapPanel;
        this.modelNode = modelNode;
        this.kind = Kind.DEFAULT;
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
     * Gets the view node's kind
     * @return the view node's kind
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * Sets the view node's kind
     * @param kind the kind to set
     */
    public void setKind(Kind kind) {
        this.kind = kind;
    }

    /**
     * Defines the node's kind
     */
    protected enum Kind {
        DEFAULT,
        DELIVERY,
        WAREHOUSE
    }
}
