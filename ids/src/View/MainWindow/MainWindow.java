package View.MainWindow;

import Model.City.Network;
import Model.Delivery.Round;
import Utils.UtilsException;
import View.MapPanel.MapPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/01/13
 * Time: 16:03
 * This class is the main window (JFrame) of the application.
 */
public class MainWindow extends JFrame {
    /**
     * Default window size and ratios
     */
    public static final double DEFAULT_RATIO = 4.0/3.0;
    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = (int)(DEFAULT_WIDTH/DEFAULT_RATIO);

    /**
     * This is the padding of the "subMainWindow (the whole window but without the topMenuBar and TopToolBar)
     */
    public static final int SUB_MAINPANEL_PADDING = 8;

    private static final String TITLE = "Supervision des livraisons";


    private Network network;
    private Round round;

    private RightPanel rightPanel = new RightPanel();
    private DeliveryListPanel deliveryListPanel = new DeliveryListPanel();

    private MapPanel mapPanel = new MapPanel();

    private TopMenuBar topMenuBar = new TopMenuBar();
    private TopToolBar topToolBar = new TopToolBar();

    private JPanel mainPanel = new JPanel(new BorderLayout());

    public MainWindow() {
        // Configures main panel
        mainPanel.add(topToolBar, BorderLayout.PAGE_START);
        mainPanel.add(createSubMainPanel(), BorderLayout.CENTER);

        // Configures main window
        setJMenuBar(topMenuBar);
        setContentPane(mainPanel);
        setTitle(TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // automatic load for map testing
        try {
            network = Network.createFromXml("../sujet/plan10x10.xml");
            mapPanel.setModel(network);
        }
        catch (UtilsException e) {
            System.out.println(e);
            //TODO: prety load
        }
    }

    public Round getRound() {
        return round;
    }

    /**
     * Creates the big panel under the two first toolbars (where most of the content of the window will be displayed)
     * @return
     */
    private JPanel createSubMainPanel() {
        JPanel subMainPanel = new JPanel(new BorderLayout());
        subMainPanel.setBorder(new EmptyBorder(SUB_MAINPANEL_PADDING, SUB_MAINPANEL_PADDING, SUB_MAINPANEL_PADDING, SUB_MAINPANEL_PADDING));
        subMainPanel.add(mapPanel, BorderLayout.CENTER);
        subMainPanel.add(deliveryListPanel, BorderLayout.WEST);
        subMainPanel.add(rightPanel, BorderLayout.EAST);

        return subMainPanel;
    }
}
