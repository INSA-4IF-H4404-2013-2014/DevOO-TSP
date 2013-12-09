package Controller;

import Controller.Command.Command;
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

        String htmlRound = round.roundToHtml();

        try {
            FileWriter outputWriter = new FileWriter(openExportFile("html"), false);
            outputWriter.write(htmlRound);
            outputWriter.close();
        } catch (java.io.IOException e) {
            System.out.println(e);
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
     * Allow user to choose a file from specified type for export
     * @param type the type of file you want the user to be able to choose
     * @return the file the user choosed
     */
    private java.io.File openExportFile(String type) {

        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter htmlExtension = new FileNameExtensionFilter(type.toUpperCase() + " Files (*." + type.toLowerCase() + ")", type.toLowerCase());
        FileNameExtensionFilter allExtension = new FileNameExtensionFilter("All files (*.*)", "");

        chooser.addChoosableFileFilter(allExtension);
        chooser.addChoosableFileFilter(htmlExtension);
        chooser.setFileFilter(htmlExtension);

        File file = chooser.getSelectedFile();

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                System.out.println(e);
            }
        } else {
            //TODO: print a confirmation window to be sure that the user wants to write over an existing file
        }

        return file;
    }

} // end of class MainWindowController --------------------------------------------------------------------

