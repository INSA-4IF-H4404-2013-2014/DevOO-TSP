package View.MainWindow;

import Model.Delivery.Round;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 09:00
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame {

    private Round round;

    DeliveryInfoPanel deliveryInfoPanel;
    DeliveryListPanel deliveryListPanel;
    RoundPanel roundPanel;
    ShortcutsPanel shortcutsPanel;

    public MainWindow() throws HeadlessException {
    }

    public Round getRound() {
        return round;
    }
}
