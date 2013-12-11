package Controller;

import Controller.Command.Command;
import Model.ChocoSolver.*;
import Model.City.Network;
import Model.City.Node;
import Model.Delivery.*;

import Utils.UtilsException;
import Utils.XmlFileFilter;

import View.MainWindow.MainWindow;
import View.MapPanel.MapPanel;
import View.MapPanel.NodeListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Deque;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 09:49
 * To change this template use File | Settings | File Templates.
 */
public class MainWindowController implements NodeListener {

    private Deque<Controller.Command.Command> historyApplied;
    private Deque<Controller.Command.Command> historyBackedOut;
    private MainWindow mainWindow;

    public MainWindowController() {
        this.mainWindow = new MainWindow(this);
        this.mainWindow.getMapPanel().setNodeEventListener(this);

        historyApplied = new LinkedList<Controller.Command.Command>();
        historyBackedOut = new LinkedList<Controller.Command.Command>();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /**
     * Action triggered when user wants to load a map
     */
    public void loadNetwork() {
        File xmlFile = openXMLFile();

        if(xmlFile != null) {
            try {
                Network network = Network.createFromXml(xmlFile.getAbsolutePath());
                mainWindow.setNetwork(network);

                // Map has been successfully loaded, we enable 'load round' feature.
                mainWindow.featureLoadRoundSetEnable(true);


            } catch (UtilsException e) {
                JOptionPane.showMessageDialog(mainWindow, "Il y a eu une erreur lors du chargement de la carte.\n" +
                        e.getMessage(), "Erreur lors du chargement de la carte", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadRound() {
        File xmlFile = openXMLFile();

        if(xmlFile != null) {
            Round round = null;
            try {
                round = Round.createFromXml(xmlFile.getAbsolutePath(), mainWindow.getNetwork());
                mainWindow.setRound(round);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainWindow, "Il y a eu une erreur lors du chargement de la tournée.\n" +
                        e.getMessage(), "Erreur lors du chargement de la tournée", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Export the current round in a HTML file choosen by user
     */
    public void exportRound() {
        Round round = this.mainWindow.getRound();

        if(round != null) {
            String htmlRound = round.roundToHtml();

            try {
                FileWriter outputWriter = new FileWriter(openFile("html"), false);
                outputWriter.write(htmlRound);
                outputWriter.close();
            } catch (java.io.IOException e) {
                System.out.println(e);
            }
        } else {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Aucune tournée n'est active", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addDelivery() {
        DeliveryDialogController deliveryDialogController = new DeliveryDialogController(mainWindow);
        deliveryDialogController.show();
    }

    public void removeDelivery() {

    }

    public void historyDo(Command command){
        historyBackedOut.clear();

        command.Apply();
        historyApplied.addLast(command);
    }

    public void historyRedo(){
        Command command = historyBackedOut.pollFirst();

        if (command == null) {
            return;
        }

        command.Apply();
        historyApplied.addLast(command);
    }

    public void historyUndo(){
        Command command = historyApplied.pollLast();

        if (command == null) {
            return;
        }

        command.Reverse();
        historyBackedOut.addFirst(command);
    }

    public void exit() {
        System.exit(0);
    }

    /**
     * Compute the actual round to find the best delivery plan. Calls the view to print it if it has been found.
     */
    public void computeRound(Network network, Round round) {
        ChocoGraph graph = new ChocoGraph(network, round);

        TSP tsp = solveTsp(graph, 100);
        SolutionState solutionState = tsp.getSolutionState();

        if((solutionState == SolutionState.SOLUTION_FOUND) || (solutionState == SolutionState.OPTIMAL_SOLUTION_FOUND)) {
            CalculatedRound calculatedRound = createCalculatedRound(tsp, graph);
            mainWindow.getMapPanel().setRound(calculatedRound);
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Aucune trajet trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void nodeClicked(MapPanel panel, Node node) {
        panel.setSelectedNode(node);
        if (!(null == this.mainWindow.getRound())) {
            Delivery del;
            if (null == (del =this.mainWindow.getRound().findDelivered(node.getId()))) {
                //if the node doesnt contains a delivery activate the "ajouter" button
                mainWindow.featureAddSetEnable(true);
                mainWindow.featureDeleteSetEnable(false);
                mainWindow.getRightPanel().getDeliveryInfoPanel().emptyFields();
            }
            else
            {
                //if the node contains a delivery activate the "supprimer" button and maj delivery info panel
                mainWindow.featureAddSetEnable(false);
                mainWindow.featureDeleteSetEnable(true);
                mainWindow.getRightPanel().getDeliveryInfoPanel().fillDeliveryInfoPanel(del);
            }
        }
    }

    /**
     * Allow user to choose a file from specified type
     * @param type the type of file you want the user to be able to choose
     * @return the file the user choosed
     */
    private File openFile(String type) {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter allExtension = new FileNameExtensionFilter("All files (*.*)", "");
        chooser.addChoosableFileFilter(allExtension);

        chooser = addExtensionType(type, chooser);

        File file = chooser.getSelectedFile();

        if(createFile(file)) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * Displays a JFileChooser with an XmlFileFilter only.
     * @return the chosen XML file, or null if no valid file has been opened.
     */
    private File openXMLFile() {
        JFileChooser chooser = new JFileChooser();
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
     * Add an extension type to a JFileChooser
     * @param type the type to add
     * @param chooser the JFileChooser to be modified
     * @return the modified JFileChooser
     */
    private JFileChooser addExtensionType(String type, JFileChooser chooser) {
        if(type.length() != 0) {
            FileNameExtensionFilter typeExtension = new FileNameExtensionFilter(type.toUpperCase() + " Files (*." + type.toLowerCase() + ")", type.toLowerCase());
            chooser.addChoosableFileFilter(typeExtension);
            chooser.setFileFilter(typeExtension);
        }
        return chooser;
    }

    /**
     * Create a new file based on file or ask to user if he wants to erase it if it already exists
     * @param file the file to be created
     * @return true if file has been created, false otherwise
     */
    private Boolean createFile(File file) {
        String message = "Êtes-vous sûr de vouloir écraser ce fichier ?";

        if(!file.exists()) {
            try {
                file.createNewFile();
                return true;
            } catch(IOException e) {
                System.out.println(e);
                return false;
            }
        } else {
            return askConfirmation(message);
        }
    }

    /**
     * Print a confirmation window with a message
     * @param message the message printed in the window
     * @return true if YES is selected, false otherwise
     */
    private Boolean askConfirmation(String message) {
        JOptionPane confirmationWindow = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        Object value = confirmationWindow.getValue();

        if(value == (Object) JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
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

        String message = "Un trajet non optimal a été trouvé. Continuer à chercher un trajet optimal ?";

        int bound = graph.getNbVertices() * graph.getMaxArcCost() + 1;

        tsp.solve(baseTime, bound);
        SolutionState solutionState = tsp.getSolutionState();

        for( ; (solutionState != SolutionState.OPTIMAL_SOLUTION_FOUND) && (solutionState != SolutionState.INCONSISTENT) && (baseTime <= 400) ; baseTime *= 2) {
            tsp.solve(baseTime, bound);
            solutionState = tsp.getSolutionState();
        }

        for( ; (tsp.getSolutionState() == SolutionState.SOLUTION_FOUND) && (askConfirmation(message)) ; baseTime *= 2) {
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
        int[] nodeList = tsp.getNext(); // ordered node list
        List<Delivery> deliveriesList = new ArrayList<Delivery>();
        List<Itinerary> itinerariesList = new ArrayList<Itinerary>();

        // Temporary variables
        ChocoDelivery chocoDelivery;
        Delivery delivery;

        for(int i = 0 ; i <= nodeList.length ; i++) {

            //TODO: be sure that the delivery added to deliveriesList is not erased at each iteration (Java pointers...)
            chocoDelivery = chocoGraph.getDelivery(nodeList[i]);
            delivery = chocoDelivery.getDelivery();
            deliveriesList.add(delivery);

            itinerariesList.add(chocoDelivery.getItinerary(delivery.getAddress().getId()));
        }

        CalculatedRound calculatedRound = new CalculatedRound(mainWindow.getRound().getWarehouse(), deliveriesList, itinerariesList);
        return calculatedRound;
    }

} // end of class MainWindowController --------------------------------------------------------------------

