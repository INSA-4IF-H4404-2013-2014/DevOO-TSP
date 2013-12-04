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

    /**
     * Draw the global view of the map in the top right corner
     */
    protected void drawGlobalView() {
        double graphDiagonal = Math.sqrt((double)(modelSize.width * modelSize.width + modelSize.height * modelSize.height));
        double graphViewFactor = globalViewMaxDiagonal / graphDiagonal;

        int globalViewWidth = (int)((double)modelSize.width * graphViewFactor);
        int globalViewHeight = (int)((double)modelSize.height * graphViewFactor);
        int globalViewPosX = this.mapPanel.getWidth() - globalViewWidth - globalViewBorderOffset;

        int modelMinX = (int)(graphViewFactor * (double)this.mapPanel.modelCoordinateX(0));
        int modelMinY = (int)(graphViewFactor * (double)this.mapPanel.modelCoordinateY(0));

        int modelMaxX = (int)(graphViewFactor * (double)this.mapPanel.modelCoordinateX(mapPanel.getWidth()));
        int modelMaxY = (int)(graphViewFactor * (double)this.mapPanel.modelCoordinateY(mapPanel.getHeight()));

        modelMinX = Math.min(Math.max(modelMinX, 0), globalViewWidth);
        modelMinY = Math.min(Math.max(modelMinY, 0), globalViewHeight);
        modelMaxX = Math.min(Math.max(modelMaxX, 0), globalViewWidth);
        modelMaxY = Math.min(Math.max(modelMaxY, 0), globalViewHeight);

        context.setColor(globalViewBackgroundColor);
        context.fillRect(globalViewPosX, globalViewBorderOffset, globalViewWidth, globalViewHeight);

        context.setColor(globalViewForegroundColor);
        context.fillRect(globalViewPosX + modelMinX, globalViewBorderOffset + modelMinY, modelMaxX - modelMinX, modelMaxY - modelMinY);
    }

    /**
     * Draw north arrow in the top left corner
     */
    protected void drawNorthArrow() {
        int leftTriangleX[] = { 0, 10, 10 };
        int rightTriangleX[] = { 20, 10, 10 };
        int triangleY[] = { 25, 20, 0 };
        int whiteTriangleX[] = { 2, 10, 10 };
        int whiteTriangleY[] = { 23, 19, 2 };

        context.translate(compassRoseBorderOffset, compassRoseBorderOffset);

        context.setColor(compassRoseBlackColor);
        context.fillPolygon(leftTriangleX, triangleY, 3);
        context.fillPolygon(rightTriangleX, triangleY, 3);
        context.drawString("N", 6, 38);

        context.setColor(compassRoseWhiteColor);
        context.fillPolygon(whiteTriangleX, whiteTriangleY, 3);

        context.translate(-compassRoseBorderOffset, -compassRoseBorderOffset);
    }

    /**
     * Draw the scale in the bottom left corner
     */
    protected void drawScale() {
        int offsetX = scaleBorderOffset;
        int offsetY = mapPanel.getHeight() - scaleBorderOffset;

        int meters = (int)((double)scaleMaxWidth / modelViewScaleFactor);

        context.setColor(scaleColor);
        context.fillRect(offsetX + 1, offsetY - scaleThickness, scaleMaxWidth - 2, scaleThickness);
        context.fillRect(offsetX, offsetY - scaleEndsHight, 1, scaleEndsHight);
        context.fillRect(offsetX + scaleMaxWidth - 1, offsetY - scaleEndsHight, 1, scaleEndsHight);
        context.drawString(meters + "m", offsetX + 10, offsetY - 10);
    }


    /** border padding in the view basis */
    protected static final int borderPadding = 50;

    /** none ground color */
    private static final Color noneground = new Color(70, 70, 70);

    /** background color */
    private static final Color background = new Color(228, 223, 211);

    /** node radius in the model's basis */
    private static final int nodeModelRadius = 12;

    /** node radius in the graph */
    private static final Color nodeColor = new Color(255, 160, 80);

    /** arc width in the model's basis */
    private static final double arcModelThickness = 2.5;

    /** global view's constants */
    private static final Color globalViewBackgroundColor = new Color(0, 0, 0, 150);
    private static final Color globalViewForegroundColor = new Color(255, 255, 255);
    private static final double globalViewMaxDiagonal = 160.0;
    private static final int globalViewBorderOffset = 10;

    /** north arrow's constants */
    private static final int compassRoseBorderOffset = 20;
    private static final Color compassRoseWhiteColor = new Color(255, 255, 255);
    private static final Color compassRoseBlackColor = new Color(0, 0, 0, 150);

    /** scale constants */
    private static final int scaleMaxWidth = 160;
    private static final int scaleBorderOffset = 20;
    private static final Color scaleColor = new Color(0, 0, 0, 150);
    private static final int scaleThickness = 3;
    private static final int scaleEndsHight = 5;
}