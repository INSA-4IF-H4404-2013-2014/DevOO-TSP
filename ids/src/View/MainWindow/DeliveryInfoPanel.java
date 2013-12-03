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
    }
}
