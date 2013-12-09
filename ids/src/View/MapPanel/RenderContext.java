package View.MapPanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
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

    /** view/model scale factor */
    private double modelViewScaleFactor;

    /** the map panel being rendered */
    MapPanel mapPanel;

    /** the java graphic context */
    Graphics2D context;

    /** parent transform */
    private AffineTransform parentTransform;

    public RenderContext(Graphics2D context, MapPanel mapPanel) {
        this.context = context;
        this.mapPanel = mapPanel;

        modelCenterPos = mapPanel.modelCenterPos;
        modelViewScaleFactor = mapPanel.modelViewScaleFactor;

        context.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        parentTransform = context.getTransform();
    }

    /**
     * set the transformation to the identity
     */
    protected void setTransformIdentity() {
        context.setTransform(parentTransform);
    }

    /**
     * set the transformation for the network drawing
     */
    protected void setTransformNetwork() {
        /*
         *  [ x']   [  m00  m01  m02  ] [ x ]
         *  [ y'] = [  m10  m11  m12  ] [ y ]
         *  [ 1 ]   [   0    0    1   ] [ 1 ]
         */
        double m00 = modelViewScaleFactor;
        double m10 = 0.0;
        double m01 = 0.0;
        double m11 = -modelViewScaleFactor;
        double m02 = (double)(mapPanel.getWidth() / 2 - (int)(modelViewScaleFactor * (double)modelCenterPos.x));
        double m12 = (double)(mapPanel.getHeight() / 2 + (int)(modelViewScaleFactor * (double)modelCenterPos.y));

        AffineTransform transform = new AffineTransform(m00, m10, m01, m11, m02, m12);

        context.setTransform(parentTransform);
        context.transform(transform);
    }

    /**
     * Draws the map's background
     */
    protected void drawBackground() {
        context.setColor(backgroundColor);
        context.fillRect(0, 0, mapPanel.getWidth(), mapPanel.getHeight());
    }

    /**
     * Draws the empty message in the center top of the map view
     */
    protected void drawEmptyMessage() {
        final String message = "Aucune carte.";

        context.setFont(new Font("default", Font.BOLD, 30));

        int messageWidth = context.getFontMetrics().stringWidth(message);

        int offsetX = (mapPanel.getWidth() - messageWidth) / 2;
        int offsetY = mapPanel.getHeight() / 3;

        context.setColor(textColor);
        context.drawString(message, offsetX, offsetY);
    }

    /**
     * Draws a given node
     * @param node the node to draw
     */
    protected void drawNode(Node node) {
        int x = node.getX() - streetThickness;
        int y = node.getY() - streetThickness;

        context.setColor(streetColor);
        context.fillOval(x, y, streetThickness * 2, streetThickness * 2);
    }

    /**
     * Draws a given node's borders
     * @param node the node to draw
     */
    protected void drawNodeBorders(Node node) {
        int nodeRadius = streetNodeRadius + (int)((double)streetBorderThickness / modelViewScaleFactor);
        int x = node.getX() - nodeRadius;
        int y = node.getY() - nodeRadius;

        if(node == mapPanel.selectedNode) {
            int nodeRadiusAdd = (int)((double)streetSelectedNodeRadiusPx / modelViewScaleFactor);

            x -= nodeRadiusAdd;
            y -= nodeRadiusAdd;

            nodeRadius += nodeRadiusAdd;

            context.setColor(streetSelectedNodeColor);
        } else {
            context.setColor(streetBorderColor);
        }

        context.fillOval(x, y, nodeRadius * 2, nodeRadius * 2);
    }

    /**
     * Draws a given arc
     * @param arc the arc to draw
     */
    protected void drawArc(Arc arc) {
        ArcInfo arcInfo = arcInfo(arc);

        context.setColor(streetColor);

        drawArcInfo(arcInfo, true);
    }

    /**
     * Draws a street name for a given arc
     * @param arc the arc you want to draw the street
     */
    protected void drawArcStreetName(Arc arc) {
        ArcInfo arcInfo = arcInfo(arc);

        Model.City.Street street = arc.getModelStreet();

        Font previousFont = context.getFont();

        context.setFont(new Font("default", Font.BOLD, 4));

        int streetNameWidth = context.getFontMetrics().stringWidth(street.getName());

        if(arcInfo.length - 2.0 * streetNodeRadius < (double)streetNameWidth) {
            context.setFont(previousFont);
            return;
        }

        int centerX = (arcInfo.x1 + arc.getNode2().getX()) / 2;
        int centerY = (arcInfo.y1 + arc.getNode2().getY()) / 2;

        double angle = arcInfo.angle;
        int streetNameYOffset = 2 * streetThickness;

        if(angle > 0.5 * Math.PI || angle < -0.5 * Math.PI) {
            angle += Math.PI;
        }

        if(arcInfo.isBidirectional()) {
            streetNameYOffset += streetThickness / 2;
        }

        AffineTransform previousTransform = context.getTransform();

        context.setColor(textColor);
        context.translate(centerX, centerY);
        context.rotate(angle);
        context.scale(1.0, -1.0);

        context.drawString(street.getName(), - streetNameWidth / 2, streetNameYOffset);

        context.setTransform(previousTransform);
        context.setFont(previousFont);
    }

    /**
     * Draws a given arc
     * @param arc the arc to draw
     */
    protected void drawArcBorders(Arc arc) {
        ArcInfo arcInfo = arcInfo(arc);

        if(arcInfo.isBidirectional()) {
            arcInfo.thickness += (double)streetBorderThickness / modelViewScaleFactor;
        } else {
            arcInfo.thickness += 2.0 * (double)streetBorderThickness / modelViewScaleFactor;
        }

        context.setColor(streetBorderColor);

        drawArcInfo(arcInfo, false);
    }

    /**
     * Draw the global view of the map in the top right corner
     */
    protected void drawGlobalView() {
        Dimension modelSize = mapPanel.getModelDimension();

        double graphDiagonal = Math.sqrt((double)(modelSize.width * modelSize.width + modelSize.height * modelSize.height));
        double graphViewFactor = globalViewMaxDiagonal / graphDiagonal;

        int globalViewWidth = (int)((double)modelSize.width * graphViewFactor);
        int globalViewHeight = (int)((double)modelSize.height * graphViewFactor);
        int globalViewPosX = mapPanel.getWidth() - globalViewWidth - globalViewBorderOffset;

        int modelMinX = (int)(graphViewFactor * (double)(mapPanel.modelCoordinateX(0) - mapPanel.modelMinPos.x));
        int modelMinY = globalViewHeight - (int)(graphViewFactor * (double)(mapPanel.modelCoordinateY(0) - mapPanel.modelMinPos.y));

        int modelMaxX = (int)(graphViewFactor * (double)(mapPanel.modelCoordinateX(mapPanel.getWidth()) - mapPanel.modelMinPos.x));
        int modelMaxY = globalViewHeight - (int)(graphViewFactor * (double)(mapPanel.modelCoordinateY(mapPanel.getHeight()) - mapPanel.modelMinPos.y));

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

    /**
     * Generates Arc's rendering informations
     * @param arc the given arc we want to render
     * @return an ArcInfo structure containing all informations
     */
    private ArcInfo arcInfo(Arc arc) {
        ArcInfo arcInfo = new ArcInfo();

        arcInfo.arc = arc;

        Node node1 = arc.getNode1();
        Node node2 = arc.getNode2();

        arcInfo.x1 = node1.getX();
        arcInfo.y1 = node1.getY();

        int nx = node2.getX() - arcInfo.x1;
        int ny = node2.getY() - arcInfo.y1;

        arcInfo.angle = Math.atan2((double)ny, (double)nx);
        arcInfo.length = Math.sqrt((double)(nx * nx + ny * ny));
        arcInfo.thickness = (double)streetThickness;

        return arcInfo;
    }

    /**
     * Draws the given arc info
     * @param arcInfo the arc's information to draw
     * @param drawMarks boolean to say if you want to draw street marks
     */
    private void drawArcInfo(ArcInfo arcInfo, boolean drawMarks) {
        Rectangle2D.Double rect = new Rectangle2D.Double();

        if(arcInfo.isBidirectional()) {
            rect.setRect(0.0, - arcInfo.thickness, arcInfo.length, 2.0 * arcInfo.thickness);
        } else {
            rect.setRect(0.0, -0.5 * arcInfo.thickness, arcInfo.length, arcInfo.thickness);
        }

        context.translate(arcInfo.x1, arcInfo.y1);
        context.rotate(arcInfo.angle);

        context.fill(rect);

        if(arcInfo.isBidirectional() && drawMarks) {
            rect.setRect((double)streetNodeRadius, -0.5 * streetCenterLineThickness, arcInfo.length - 2.0 * (double)streetNodeRadius, streetCenterLineThickness);

            context.setColor(streetMarksColor);
            context.fill(rect);
        } else {
            Path2D.Double path = new Path2D.Double();

            path.moveTo(arcInfo.length - (double)streetNodeRadius, -0.5 * arcInfo.thickness);
            path.lineTo(arcInfo.length, -0.5 * arcInfo.thickness);
            path.lineTo(arcInfo.length, 0.5 * arcInfo.thickness);
            path.lineTo(arcInfo.length - (double)streetNodeRadius, 0.5 * arcInfo.thickness);
            path.lineTo(arcInfo.length - (double)streetNodeRadius + 0.5 * arcInfo.thickness, 0.0);
            path.closePath();

            context.setColor(streetMarksColor);
            context.fill(path);
        }

        context.rotate(-arcInfo.angle);
        context.translate(-arcInfo.x1, -arcInfo.y1);
    }

    /**
     * Arc rendering information
     */
    private class ArcInfo {
        /** the view arc */
        public Arc arc;

        /** the arc angle */
        public double angle;

        /** the arc view length (between the two nodes) in px */
        public double length;

        /** the arc's thickness in px */
        public double thickness;

        /** the arc's start X position on screen in px */
        public int x1;

        /** the arc's start Y position on screen in px */
        public int y1;

        /**
         * Tests if the arc is a bidirectonal arc
         * @return
         *  - true if it is a bidirectional arc
         *  - false if it is an unidirectional arc
         */
        public boolean isBidirectional() {
            return (arc.getModelArcFrom1To2() != null && arc.getModelArcFrom2To1() != null);
        }
    }


    /** border padding in the view basis */
    protected static final int borderPadding = 50;

    /** background color */
    private static final Color backgroundColor = new Color(236, 232, 223);
    private static final Color textColor = new Color(0, 0, 0, 150);

    /** street's constants */
    private static final Color streetColor = new Color(255, 255, 255);
    private static final Color streetMarksColor = new Color(200, 200, 200);
    private static final Color streetBorderColor = new Color(210, 140, 100);
    private static final int streetThickness = 4;
    private static final int streetBorderThickness = 1;
    private static final double streetCenterLineThickness = 0.2;

    /** node color */
    protected static final int streetNodeRadius = 10;
    private static final Color streetSelectedNodeColor = new Color(240, 80, 80);
    private static final int streetSelectedNodeRadiusPx = 4;

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
