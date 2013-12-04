package View.MapPanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 04/12/13
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
public class RenderContext {

    /** view's center pos in the model basis */
    private Point modelCenterPos;

    /** graph model's size in the model basis */
    private Dimension modelSize;

    /** view/model scale factor */
    private double modelViewScaleFactor;

    /** global view X offset */
    int xGlobalOffset;

    /** global view Y offset */
    int yGlobalOffset;

    /** the map panel being rendered */
    MapPanel mapPanel;

    /** the java graphic context */
    Graphics2D context;

    public RenderContext(Graphics2D context, MapPanel mapPanel) {
        this.context = context;
        this.mapPanel = mapPanel;

        this.modelCenterPos = mapPanel.modelCenterPos;
        this.modelSize = mapPanel.modelSize;
        this.modelViewScaleFactor = mapPanel.modelViewScaleFactor;

        this.xGlobalOffset = this.mapPanel.getWidth() / 2 - (int)(this.modelViewScaleFactor * (double)this.modelCenterPos.x);
        this.yGlobalOffset = this.mapPanel.getHeight() / 2 - (int)(this.modelViewScaleFactor * (double)this.modelCenterPos.y);

        this.context.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * Draws the none ground
     */
    protected void drawNoneground() {
        this.context.setColor(noneground);
        this.context.fillRect(0, 0, this.mapPanel.getWidth(), this.mapPanel.getHeight());
    }

    /**
     * Draws the map's background
     */
    protected void drawBackground() {
        int viewGraphSizeWidth = (int)(this.modelViewScaleFactor * (double)this.modelSize.width) + 2 * borderPadding;
        int viewGraphSizeHeight = (int)(this.modelViewScaleFactor * (double)this.modelSize.height) + 2 * borderPadding;

        context.setColor(background);
        context.fillRect(this.xGlobalOffset - borderPadding, this.yGlobalOffset - borderPadding, viewGraphSizeWidth, viewGraphSizeHeight);
    }

    /**
     * Draws a given node
     * @param node the node to draw
     */
    protected void drawNode(Node node) {
        int nodeRadius = (int)(modelViewScaleFactor * (double)nodeModelRadius);
        int x = xGlobalOffset - nodeRadius / 2 + (int)(modelViewScaleFactor * (double)node.getX());
        int y = yGlobalOffset - nodeRadius / 2 + (int)(modelViewScaleFactor * (double)node.getY());

        context.setColor(nodeColor);
        context.fillOval(x, y, nodeRadius, nodeRadius);
    }

    /**
     * Draws a given arc
     * @param arc the arc to draw
     */
    protected void drawArc(Arc arc) {
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

        double arcThickness = this.modelViewScaleFactor * arcModelThickness;

        Rectangle2D.Double rect = new Rectangle2D.Double();
        rect.setRect(0.0, -arcThickness, nl, 2.0 * arcThickness);

        context.translate(x1, y1);
        context.rotate(angle);
        context.fill(rect);
        context.rotate(-angle);
        context.translate(-x1, -y1);
    }


    /** border padding in the view basis */
    protected static final int borderPadding = 50;

    /** none ground color */
    private static final Color noneground = new Color(70, 70, 70);

    /** background color */
    private static final Color background = new Color(255, 255, 255);

    /** node radius in the model's basis */
    private static final int nodeModelRadius = 12;

    /** node radius in the graph */
    private static final Color nodeColor = new Color(255, 160, 80);

    /** arc width in the model's basis */
    private static final double arcModelThickness = 2.5;
}
