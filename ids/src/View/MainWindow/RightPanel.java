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

    private RoundPanel roundPanel = new RoundPanel();
    private DeliveryInfoPanel deliveryInfoPanel = new DeliveryInfoPanel();

    public static final float DEFAULT_WIDTH_RATIO = (float)0.33;

    public RightPanel() {
        setPreferredSize(new Dimension((int)(MainWindow.DEFAULT_WIDTH*DEFAULT_WIDTH_RATIO), 0));

        setLayout(new GridLayout(2,1));

        add(roundPanel);
        add(deliveryInfoPanel);
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
