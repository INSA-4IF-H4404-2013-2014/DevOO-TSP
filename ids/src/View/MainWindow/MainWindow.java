package View.MainWindow;

import Controller.MainWindowController;
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
    public static final double DEFAULT_RATIO = 16.0/9.0;
    public static final int DEFAULT_WIDTH = 1280;
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

    protected MainWindowController mainWindowController;

    public MainWindow(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;

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

        addListener();

        setVisible(true);

        // automatic load for map testing
        try {
            network = Network.createFromXml("../sujet/plan10x10.xml");
            //network = Network.createFromXml("../sujet/planTiny.xml");
            mapPanel.setModel(network);
        }
        catch (UtilsException e) {
            System.out.println(e);
            //TODO: prety load
        }
    }

    /**
     * Get the round of the main window
     * @return the round of the main window
     */
    public Round getRound() {
        return round;
    }

    /**
     * Sets the round.
     * Asks the deliveryListPanel to display it.
     * @param round the round to set & display
     */
    public void setRound(Round round) {
        this.round = round;
        deliveryListPanel.setModel(round.getDeliveryDisplayableList());
    }

    /**
     * Get the network of the main window
     * @return the network of the main window
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Sets the networks.
     * Asks the mapPanel to display it.
     */
    public void setNetwork(Network net) {
        network = net;
        mapPanel.setModel(net);
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

    /**
     * Gets the menu bar at the top of the main window
     * @return the menu bar on the top of the main window
     */
    public TopMenuBar getTopMenuBar() {
        return topMenuBar;
    }

    /**
     * Gets the top toolbar of the main window
     * @return the top toolbar of the main window
     */
    public TopToolBar getTopToolBar() {
        return topToolBar;
    }

    /**
     * Gets the list panel on the left of the main window
     * @return the panel on the left
     */
    public DeliveryListPanel getDeliveryListPanel() {
        return deliveryListPanel;
    }

    /**
     * Gets the main window's map panel
     * @return the map panel
     */
    public MapPanel getMapPanel() {
        return mapPanel;
    }

    /**
     * Gets the panel on the right of the main window
     * @return the panel on the right
     */
    public RightPanel getRightPanel() {
        return rightPanel;
    }

    /**
     * Adds listeners for all buttons, lists...
     */
    public void addListener() {
        topMenuBar.addListener(mainWindowController);
        topToolBar.addListener(mainWindowController);
    }

    /**
     * Enable or disable the feature 'load a round'
     * @param b whether to enable or disable the feature
     */
    public void featureLoadRoundSetEnable(boolean b) {
        topMenuBar.openRound.setEnabled(b);
        topToolBar.loadRound.setEnabled(b);
    }
}
