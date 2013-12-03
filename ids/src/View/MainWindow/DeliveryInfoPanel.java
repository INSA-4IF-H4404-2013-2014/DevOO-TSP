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

    JTextField client;
    JTextField address;
    JTextField deliveryDate;
    JTextField delay;
    TimeFramePanel timeFrame;

    public DeliveryInfoPanel() {
        setPreferredSize(new Dimension(300, 0));
    }
}
