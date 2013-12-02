package View.MainWindow;

import Model.Delivery.Round;

import javax.swing.*;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/01/13
 * Time: 16:03
 * This class is the main window (JFrame) of the application.
 */
public class MainWindow extends JFrame {
    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 768;
    private static final String TITLE = "Supervision des livraisons";

    private Round round;

    private DeliveryInfoPanel deliveryInfoPanel = new DeliveryInfoPanel();
    private DeliveryListPanel deliveryListPanel = new DeliveryListPanel();
    private RoundPanel roundPanel = new RoundPanel();
    private ShortcutsPanel shortcutsPanel = new ShortcutsPanel();

    private TopMenuBar topMenuBar = new TopMenuBar();
    private TopToolBar topToolBar = new TopToolBar();

    private JPanel mainPanel = new JPanel(new BorderLayout());

    public MainWindow() {
        // Configures main panel
        mainPanel.add(topToolBar, BorderLayout.PAGE_START);

        // Configures main window
        setJMenuBar(topMenuBar);
        setContentPane(mainPanel);
        setTitle(TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public Round getRound() {
        return round;
    }
}
