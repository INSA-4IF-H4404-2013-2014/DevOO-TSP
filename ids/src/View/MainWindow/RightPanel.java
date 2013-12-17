package View.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 07/12/13
 * Time: 13:53
 * This class is the main window (JFrame) of the application.
 */
public class RightPanel extends JPanel {

    /** Panel of the round summary (top right of the main window) */
    private RoundPanel roundPanel = new RoundPanel();

    /** Panel of the delivery's info (bottom right of the main window) */
    private DeliveryInfoPanel deliveryInfoPanel = new DeliveryInfoPanel();

    /** Ratio of main window's width this right panel will take */
    public static final float DEFAULT_WIDTH_RATIO = (float)0.30;

    /**
     * Constructor
     */
    public RightPanel() {
        setPreferredSize(new Dimension((int)(MainWindow.DEFAULT_WIDTH*DEFAULT_WIDTH_RATIO), 0));

        roundPanel.setMinimumSize(new Dimension(200, 200));
        deliveryInfoPanel.setMinimumSize(new Dimension(200, 200));

        JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, roundPanel, deliveryInfoPanel);
        jsp.setMinimumSize(new Dimension(285, 100));
        jsp.setPreferredSize(new Dimension(100, 100));
        jsp.setDividerLocation(250);

        setLayout(new BorderLayout());
        add(jsp, BorderLayout.CENTER);
    }

    /**
     * Gets the round panel (the one on the top right)
     * @return the round panel
     */
    public RoundPanel getRoundPanel() {
        return roundPanel;
    }

    /**
     * Gets the delivery info panel (the one on the bottom right)
     * @return the delivery info panel
     */
    public DeliveryInfoPanel getDeliveryInfoPanel() {
        return deliveryInfoPanel;
    }
}
