package Controller;

import Controller.Command.Command;
import Model.City.Node;
import View.MainWindow.MainWindow;
import View.MapPanel.MapPanel;
import View.MapPanel.NodeListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class MainWindowController implements MouseListener, NodeListener, ListSelectionListener {

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

    public void exportRound() {

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
} // end of class MainWindowController --------------------------------------------------------------------

