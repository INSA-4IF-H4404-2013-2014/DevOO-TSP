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
    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 768;

    private Round round;

    private DeliveryInfoPanel deliveryInfoPanel;
    private DeliveryListPanel deliveryListPanel;
    private RoundPanel roundPanel;
    private ShortcutsPanel shortcutsPanel;

    public MainWindow() throws HeadlessException {
        setTitle("Supervision des livraisons");
        setSize(1024,768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public Round getRound() {
        return round;
    }
}
