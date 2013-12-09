package Controller;

import Controller.Command.Command;
import Model.ChocoSolver.ChocoGraph;
import Model.ChocoSolver.Graph;
import Model.ChocoSolver.SolutionState;
import Model.ChocoSolver.TSP;
import Model.City.Node;
import Model.Delivery.Round;
import Model.Delivery.Schedule;
import Model.Delivery.Delivery;
import Model.Delivery.Client;
import View.MainWindow.MainWindow;
import View.MapPanel.MapPanel;
import View.MapPanel.NodeListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
public class MainWindowController implements MouseListener, NodeListener, ListSelectionListener, ActionListener {

    private Deque<Controller.Command.Command> historyApplied;
    private Deque<Controller.Command.Command> historyBackedOut;
    private MainWindow mainWindow;

    public MainWindowController() {
        this.mainWindow = new MainWindow();
        this.mainWindow.getMapPanel().setNodeEventListener(this);

        historyApplied = new LinkedList<Controller.Command.Command>();
        historyBackedOut = new LinkedList<Controller.Command.Command>();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void loadNetwork() {

    }

    public void loadRound() {

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

    /**
     *
     */
    public void computeRound() {
        Graph graph = new ChocoGraph(mainWindow.getNetwork(), mainWindow.getRound());
        TSP tsp = new TSP(graph);

        Boolean ok = true;

        String message = "Un trajet non optimal a été trouvé. Continuer à chercher un trajet optimal ?";

        int baseTime = 100;
        int bound = graph.getNbVertices() * graph.getMaxArcCost() + 1;

        tsp.solve(baseTime, bound);

        for( ; (tsp.getSolutionState() != SolutionState.OPTIMAL_SOLUTION_FOUND) && (baseTime <= 400) ; baseTime *= 2) {
            tsp.solve(baseTime, bound);
        }

        for( ; (tsp.getSolutionState() == SolutionState.SOLUTION_FOUND) && (askConfirmation(message)) ; baseTime *= 2) {
            // Retry to find an optimal solution
        }

        if((tsp.getSolutionState() == SolutionState.SOLUTION_FOUND) || (tsp.getSolutionState() == SolutionState.OPTIMAL_SOLUTION_FOUND)) {
            //TODO: get the best graph and print it
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Aucune trajet trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void nodeClicked(MapPanel panel, Node node) {
        panel.setSelectedNode(node);
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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

} // end of class MainWindowController --------------------------------------------------------------------

