package controller;

import controller.Command.AddDelivery;
import controller.Command.Command;
import controller.Command.RemoveDelivery;
import model.ChocoSolver.*;
import model.City.Network;
import model.City.Node;
import model.Delivery.*;

import utils.UtilsException;
import utils.XmlFileFilter;

import view.MainWindow.AboutDialog;
import view.MainWindow.MainWindow;
import view.MapPanel.MapPanel;
import view.MapPanel.NodeListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 09:49
 * To change this template use File | Settings | File Templates.
 */
public class MainWindowController implements NodeListener, ListSelectionListener {

    /** Commands that are currently applied */
    private Deque<controller.Command.Command> historyApplied;

    /** Commands that have been backed out by historyUndo */
    private Deque<controller.Command.Command> historyBackedOut;

    /** main.Main window */
    private MainWindow mainWindow;

    /**
     * Constructor
     */
    public MainWindowController() {
        this.mainWindow = new MainWindow(this);
        this.mainWindow.getMapPanel().setNodeEventListener(this);

        historyApplied = new LinkedList<controller.Command.Command>();
        historyBackedOut = new LinkedList<controller.Command.Command>();
    }

    /**
     * Returns the history backed out
     * @return the history backed out
     */
    public Deque<controller.Command.Command> getHistoryBackedOut() {
        return historyBackedOut;
    }

    /**
     * Returns the history applied
     * @return the history applied
     */
    public Deque<controller.Command.Command> getHistoryApplied() {
        return historyApplied;
    }

    /**
     * Returns the controller's main window
     * @return the controller's main window
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /**
     * Action triggered when user wants to load a map
     */
    public void loadNetwork() {
        File xmlFile = openXMLFile("Ouvrir une carte");

        if(xmlFile == null) {
            return;
        }

        loadNetwork(xmlFile.getAbsolutePath());
    }

    /**
     * Loads a network form a given XML path
     * @param xmlPath the xml path
     */
    public void loadNetwork(String xmlPath) {
        try {
            Network network = Network.createFromXml(xmlPath);
            mainWindow.setNetwork(network);
            mainWindow.setRound(null);
            mainWindow.setCalculatedRound(null);

            selectNode(null);
            mainWindow.getRightPanel().getRoundPanel().emptyFields();

            // Map has been successfully loaded, we enable 'load round' feature.
            mainWindow.featureLoadRoundSetEnable(true);

            // Reset Undo/Redo features
            resetUndoRedo();

            // Disable export round feature
            mainWindow.featureSaveRoundSetEnable(false);


        } catch (UtilsException e) {
            JOptionPane.showMessageDialog(mainWindow, "Il y a eu une erreur lors du chargement de la carte.\n" +
                    e.getMessage(), "Erreur lors du chargement de la carte", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Action triggered when user wants to load a round
     */
    public void loadRound() {
        File xmlFile = openXMLFile("Ouvrir une tournée");

        if(xmlFile == null) {
            return;
        }

        loadRound(xmlFile.getAbsolutePath());
    }

    /**
     * Loads a deliveries round form a given XML path
     * @param xmlPath the xml path
     */
    public void loadRound(String xmlPath) {
        mainWindow.featureSaveRoundSetEnable(false);

        try {
            mainWindow.setRound(Round.createFromXml(xmlPath, mainWindow.getNetwork()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainWindow, "Le chargement de la tournée a retourné une erreur :\n" +
                    e.getMessage(), "Erreur lors du chargement de la tournée", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            computeRound(this.getMainWindow().getNetwork(), this.getMainWindow().getRound());

            selectNode(null);

            mainWindow.getRightPanel().getRoundPanel().emptyFields();
            mainWindow.getRightPanel().getRoundPanel().fillRoundPanel(this.mainWindow.getCalculatedRound());

            mainWindow.featureSaveRoundSetEnable(true);
            resetUndoRedo();

        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindow, "Une erreur est survenue lors du calcul de la tournée",
                    "Erreur lors du calcul de la tournée", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Export the current round in a HTML file choosen by user
     */
    public void exportRound() {
        CalculatedRound calculatedRound = this.mainWindow.getCalculatedRound();

        if(calculatedRound != null) {
            String htmlRound = calculatedRound.calculatedRoundToHtml();

            try {
                File file = saveFileDialog();
                if(file == null) {
                    return;
                }

                FileWriter outputWriter = new FileWriter(file, false);
                outputWriter.write(htmlRound);
                outputWriter.close();
            } catch (java.io.IOException e) {
                System.out.println(e);
            }
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Aucune tournée n'est active", "Erreur d'export", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens the add delivery window
     */
    public void addDelivery() {
        DeliveryDialogController deliveryDialogController = new DeliveryDialogController(mainWindow);
        deliveryDialogController.show();
        if(deliveryDialogController.addIsReady()) {
            AddDelivery add = new AddDelivery(this, deliveryDialogController.getClient(), deliveryDialogController.getAddress(), deliveryDialogController.getBegin(), deliveryDialogController.getEnd());
            this.historyDo(add);
            try {
                selectNode(mainWindow.getMapPanel().getSelectedNodeView());
            } catch(Exception e) {}
        }
    }

    /**
     * Creates a removeDelivery command, adds it to the history and apply it
     */
    public void removeDelivery() {
        int idNode = this.mainWindow.getMapPanel().getSelectedNodeView().getId();
        RemoveDelivery remove = RemoveDelivery.create(this, idNode);
        this.historyDo(remove);
    }

    /**
     * Adds a commands to the current applied history and clear the backed out history
     * @param command the command to add
     */
    public void historyDo(Command command){
        command.Apply();
        historyBackedOut.clear();
        historyApplied.push(command);
        updateUndoRedoButtons();
    }

    /**
     * Redo the first command available in the backed out history
     */
    public void historyRedo(){
        Command command = historyBackedOut.pop();

        if (command == null) {
            return;
        }

        command.Apply();
        historyApplied.push(command);
        updateUndoRedoButtons();
        selectNode(null);
    }

    /**
     * Undo the last command available in the applied history and move it to the front of the backed out history
     */
    public void historyUndo(){
        Command command = historyApplied.pop();

        if (command == null) {
            return;
        }

        command.Reverse();
        historyBackedOut.push(command);
        updateUndoRedoButtons();
        selectNode(null);
    }

    /**
     * Shows the 'about' dialog
     */
    public void showAboutDialog() {
        AboutDialog aboutDialog = new AboutDialog(mainWindow);
        aboutDialog.setVisible(true);
    }

    /**
     * Asks the application to exit in a clean way.
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Compute the actual round to find the best delivery plan. Calls the view to print it if it has been found.
     */
    public int computeRound(Network network, Round round) {
        TSP tsp = null;
        SolutionState solutionState;
        ChocoGraph graph = new ChocoGraph(network, round);

        if(round.getDeliveryList().size() != 0) {
            tsp = solveTsp(graph, 100);
            solutionState = tsp.getSolutionState();
        }
        else {
            solutionState = SolutionState.OPTIMAL_SOLUTION_FOUND;
        }

        if((solutionState == SolutionState.SOLUTION_FOUND) || (solutionState == SolutionState.OPTIMAL_SOLUTION_FOUND)) {
            CalculatedRound calculatedRound = createCalculatedRound(tsp, graph);
            mainWindow.getMapPanel().setRound(calculatedRound);
            mainWindow.setCalculatedRound(calculatedRound);
            mainWindow.getRightPanel().getRoundPanel().fillRoundPanel(this.mainWindow.getCalculatedRound());
            return 0;
        }

        JOptionPane.showMessageDialog(mainWindow, "Aucun trajet trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
        CalculatedRound calculatedRound = createCalculatedRound(null, graph);
        mainWindow.getMapPanel().setRound(calculatedRound);
        mainWindow.setCalculatedRound(calculatedRound);
        mainWindow.getRightPanel().getRoundPanel().fillRoundPanel(this.mainWindow.getCalculatedRound());

        return 1;
    }

    /**
     * Gets triggered when a node has been clicked.
     * @param node the model node that has been clicked
     */
    @Override
    public void nodeClicked(MapPanel panel, Node node) {
        mainWindow.getDeliveryListPanel().clearSelection();

        selectNode(node);
    }

    /**
     * Gets triggered when the map panel's background has been clicked.
     * @param panel the map panel that has received the mouse clicked event
     */
    @Override
    public void backgroundClicked(MapPanel panel) {
        mainWindow.getDeliveryListPanel().clearSelection();

        selectNode(null);
    }

    /**
     * Gets triggered when the selection changes in DeliveryListPanel's JList
     * @param e event
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            return;
        }

        Delivery selectedDelivery = mainWindow.getDeliveryListPanel().getSelectedValue();
        if(selectedDelivery != null) {
            Node selectedNode = selectedDelivery.getAddress();
            selectNode(selectedNode);

            // We only want to move the map if the node is not already visible.
            if(!mainWindow.getMapPanel().isVisible(selectedNode)) {
                mainWindow.getMapPanel().centerOn(selectedNode);
            }
        }
        else {
            selectNode(null);
        }
    }

    /**
     * Apply actions following a node selection from the DeliveryListPanel or MapPanel
     * @param node
     */
    private void selectNode(Node node) {
        mainWindow.getMapPanel().setSelectedNodeView(node);

        if(node == null) {
            // No node is selected. So we don't want to let user add delivery to nowhere.
            mainWindow.featureAddSetEnable(false);
            mainWindow.featureDeleteSetEnable(false);
            mainWindow.getRightPanel().getDeliveryInfoPanel().emptyFields();

            return;
        }

        if (this.mainWindow.getRound() != null) {
            Delivery del = mainWindow.getRound().findDelivered(node.getId());

            if (del == null) {
                // If the node doesn't contain a delivery, activate the "ajouter" button (only when not warehouse)
                mainWindow.featureAddSetEnable(node.getId() != mainWindow.getRound().getWarehouse().getId());
                mainWindow.featureDeleteSetEnable(false);
                mainWindow.getRightPanel().getDeliveryInfoPanel().emptyFields();
                mainWindow.getRightPanel().getDeliveryInfoPanel().fillDeliveryInfoPanelNonDeliveryNode(node);
            }
            else
            {
                // If the node contains a delivery, activate the "supprimer" button and maj delivery info panel
                mainWindow.featureAddSetEnable(false);
                mainWindow.featureDeleteSetEnable(true);
                mainWindow.getRightPanel().getDeliveryInfoPanel().fillDeliveryInfoPanel(del, mainWindow.getCalculatedRound());
                mainWindow.getDeliveryListPanel().setSelectedValue(del);
            }
        }
    }

    /**
     * Display a JFileChooser in 'save file' mode.
     * @return the file the user chose
     */
    private File saveFileDialog() {
        JFileChooser chooser = new JFileChooser() {
            public void approveSelection() {
                File f = getSelectedFile();
                if(f.exists()) {
                    int result = JOptionPane.showConfirmDialog
                            (
                                    this,
                                    "Êtes-vous sûr de vouloir écraser le fichier sélectionné ?",
                                    "Fichier existant",
                                    JOptionPane.YES_NO_OPTION
                            );
                    switch(result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;

                        case JOptionPane.NO_OPTION:
                        default:
                            return;
                    }
                } else {
                    super.approveSelection();
                }
            }
        };
        chooser.setCurrentDirectory(new File("../"));
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(new FileNameExtensionFilter("Pages Web HTML (*.html)", "html"));
        chooser.setDialogTitle("Exporter une tournée calculée");
        chooser.setSelectedFile(new File(".html"));

        int feedback = chooser.showSaveDialog(mainWindow);
        if(feedback == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }

    /**
     * Displays a JFileChooser with an XmlFileFilter only.
     * @param title The JFileChooser's title
     * @return the chosen XML file, or null if no valid file has been opened.
     */
    private File openXMLFile(String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        chooser.setCurrentDirectory(new File("../"));
        chooser.setFileFilter(new XmlFileFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        int feedback = chooser.showOpenDialog(mainWindow);
        if(feedback == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }

    /**
     * Print a confirmation window with a message
     * @param message the message printed in the window
     * @return true if YES is selected, false otherwise
     */
    private Boolean askConfirmation(String message) {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                message,
                "Temps insuffisant",
                JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {
            return true;
        }

        return false;
    }

    /**
     * Try to find the optimal solution for the TSP
     * If no optimal solution is found, search time will be multiplied by two twice
     * If a solution, which not optimal, is found, ask to the user if he wants to give more time for optimal solution search
     * @param graph the graph on which you want to search
     * @param baseTime the time you want to allow for the search (time will be doubled twice if no solution has been founded)
     * @return the solved TSP
     */
    private TSP solveTsp(Graph graph, int baseTime) {
        TSP tsp = new TSP(graph);

        String messageNotFound = "Aucun trajet n'a été trouvé dans le temps imparti. Continuer à chercher un trajet ?";
        String messageNotOptimal = "Un trajet non optimal a été trouvé dans le temps imparti. Continuer à chercher un trajet optimal ?";

        int bound = graph.getNbVertices() * graph.getMaxArcCost() + 1;

        tsp.solve(baseTime, bound-1);
        SolutionState solutionState = tsp.getSolutionState();

        for( ; (solutionState != SolutionState.OPTIMAL_SOLUTION_FOUND) && (solutionState != SolutionState.INCONSISTENT) && (baseTime <= 400) ; baseTime *= 2) {
            tsp.solve(baseTime, bound);
            solutionState = tsp.getSolutionState();
        }

        for( ; (tsp.getSolutionState() == SolutionState.SOLUTION_FOUND && askConfirmation(messageNotOptimal)) ||
                (tsp.getSolutionState() == SolutionState.NO_SOLUTION_FOUND && askConfirmation(messageNotFound)) ; baseTime *= 2) {
            tsp.solve(baseTime, bound);
        }

        return tsp;
    }

    /**
     * Create a CalculatedRound from a TSP and a ChocoGraph
     * @param tsp the tsp used to create the CalculatedRound
     * @param chocoGraph the chocoGraph used to create the CalculatedRound
     * @return the CalculatedRound
     */
    private CalculatedRound createCalculatedRound(TSP tsp, ChocoGraph chocoGraph) {
        if(tsp != null) {
            return new CalculatedRound(mainWindow.getRound().getWarehouse(), tsp.getNext(), chocoGraph);
        }

        return new CalculatedRound(mainWindow.getRound().getWarehouse(), null, chocoGraph);
    }

    /**
     * Update the enable/disable status of undo & redo buttons.
     */
    private void updateUndoRedoButtons() {
        mainWindow.featureUndoSetEnable(historyApplied.size() > 0);
        mainWindow.featureRedoSetEnable(historyBackedOut.size() > 0);

    }

    /**
     * Clears undo/redo stacks
     * Updates enable/disable state.
     */
    private void resetUndoRedo() {
        historyApplied.clear();
        historyBackedOut.clear();
        updateUndoRedoButtons();
    }
} // end of class MainWindowController --------------------------------------------------------------------

