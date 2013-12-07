package View.MainWindow;

import View.TimeFramePanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/12/13
 * Time: 21:15
 * This is the right side of the main window
 */
public class DeliveryInfoPanel extends JPanel {

    public static final float DEFAULT_WIDTH_RATIO = (float)0.3;

    private JTextField client;
    private JTextField address;
    private JTextField deliveryDate;
    private JTextField delay;
    private TimeFramePanel timeFrame;

    public DeliveryInfoPanel() {
        setPreferredSize(new Dimension((int)(MainWindow.DEFAULT_WIDTH*DEFAULT_WIDTH_RATIO), 0));
        setLayout(new GridLayout(2,1));
        add(createTopDeliveryInfoPanel());
        add(createBottomDeliveryInfoPanel());
    }

    /**
     * Creates top part of the delivery info panel (this is the top most right part
     * in the main window).
     *
     * @return the JPanel
     */
    private JPanel createTopDeliveryInfoPanel()
    {
        JPanel topDeliveryInfoPanel = new JPanel();
        topDeliveryInfoPanel.setBackground(Color.GRAY);
        return topDeliveryInfoPanel;
    }

    /**
     * Creates the bottom part of the delivery info panel (this is the bottom most right part
     * in the main window).
     * @return
     */
    private JPanel createBottomDeliveryInfoPanel()
    {
        JPanel bottomDeliveryInfoPanel = new JPanel();
        bottomDeliveryInfoPanel.setBackground(Color.LIGHT_GRAY);
        return bottomDeliveryInfoPanel;
    }
}
