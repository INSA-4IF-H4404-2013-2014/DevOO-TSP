package View.MainWindow;

import Controller.MainWindowController;
import Model.ChocoSolver.CalculatedRound;
import Model.City.Network;
import Model.Delivery.Delivery;
import Model.Delivery.Round;
import View.MapPanel.MapPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;

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
    private CalculatedRound calculatedRound;

    private RightPanel rightPanel = new RightPanel();
    private DeliveryListPanel deliveryListPanel = new DeliveryListPanel();

    private MapPanel mapPanel = new MapPanel();

    private TopMenuBar topMenuBar = new TopMenuBar();
    private TopToolBar topToolBar = new TopToolBar();

    private JPanel mainPanel = new JPanel(new BorderLayout());

    protected MainWindowController mainWindowController;

    /**
     * Constructor
     * @param mainWindowController The main window 's controller
     */
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
    }

    /**
     * Get the round of the main window
     * @return the round of the main window
     */
    public Round getRound() {
        return round;
    }

    /**
     * Get the calculated round of the main window
     * @return the calculated round of the main window
     */
    public CalculatedRound getCalculatedRound() {
        return this.calculatedRound;
    }

    /**
     * Sets the round.
     * Asks the deliveryListPanel to display it.
     * @param round the round to set & display
     */
    public void setRound(Round round) {
        this.round = round;
    }

    /**
     * Sets the calculated round
     * @param calculatedRound the calculated round to set & display
     */
    public void setCalculatedRound(CalculatedRound calculatedRound) {
        this.calculatedRound = calculatedRound;
        deliveryListPanel.setModel(getOrderedDeliveryList());
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
     * @return the panel
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
        deliveryListPanel.addListener(mainWindowController);
    }

    /**
     * Enable or disable the feature 'load a map'
     * @param b whether to enable or disable the feature
     */
    public void featureLoadMapSetEnable(boolean b) {
        topMenuBar.openMap.setEnabled(b);
        topToolBar.loadMap.setEnabled(b);
    }

    /**
     * Enable or disable the feature 'load a round'
     * @param b whether to enable or disable the feature
     */
    public void featureLoadRoundSetEnable(boolean b) {
        topMenuBar.openRound.setEnabled(b);
        topToolBar.loadRound.setEnabled(b);
    }

    /**
     * Enable or disable the feature 'save a round'
     * @param b whether to enable or disable the feature
     */
    public void featureSaveRoundSetEnable(boolean b) {
        topMenuBar.saveRound.setEnabled(b);
        topToolBar.saveRound.setEnabled(b);
    }

    /**
     * Enable or disable the feature 'add a delivery'
     * @param b whether to enable or disable the feature
     */
    public void featureAddSetEnable(boolean b) {
        topMenuBar.addButton.setEnabled(b);
        topToolBar.add.setEnabled(b);
    }

    /**
     * Enable or disable the feature 'add a delivery'
     * @param b whether to enable or disable the feature
     */
    public void featureDeleteSetEnable(boolean b) {
        topMenuBar.delButton.setEnabled(b);
        topToolBar.delete.setEnabled(b);
    }

    /**
     * Enable or disable the feature 'undo'
     * @param b whether to enable or disable the feature
     */
    public void featureUndoSetEnable(boolean b) {
        topMenuBar.undoButton.setEnabled(b);
        topToolBar.undo.setEnabled(b);
    }

    /**
     * Enable or disable the feature 'redo'
     * @param b whether to enable or disable the feature
     */
    public void featureRedoSetEnable(boolean b) {
        topMenuBar.redoButton.setEnabled(b);
        topToolBar.redo.setEnabled(b);
    }

    /**
     * Gets an ordered vector of deliveries.
     * The JList on the left side of the window will need this.
     * @return The vector.
     */
    private Vector<Delivery> getOrderedDeliveryList() {
        if(calculatedRound == null) {
            return new Vector<Delivery>();
        }

        java.util.List<Integer> orderedNodesId = calculatedRound.getOrderedNodesId();
        Vector<Delivery> orderedDeliveryList = new Vector<Delivery>();

        for(Integer nodeId : orderedNodesId) {
            orderedDeliveryList.add(round.findDelivered(nodeId));
        }
        return orderedDeliveryList;
    }
}
