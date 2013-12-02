package Controller;

import Controller.Command.Command;
import View.MainWindow.MainWindow;

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
public class MainWindowController implements MouseListener {

    private Deque<Controller.Command.Command> historyApplied;
    private Deque<Controller.Command.Command> historyBackedOut;
    private MainWindow mainWindow;

    public MainWindowController() {
        this.mainWindow = new MainWindow();
        historyApplied = new LinkedList<Controller.Command.Command>();
        historyBackedOut = new LinkedList<Controller.Command.Command>();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
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
    public void mouseClicked(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
