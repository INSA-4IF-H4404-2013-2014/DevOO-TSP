package Controller.Command;

import Controller.MainWindowController;
import Model.Delivery.Round;
import View.MainWindow.MainWindow;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:07
 * To change this template use File | Settings | File Templates.
 */
public class Command {

    /** controller to apply the command */
    Controller.MainWindowController controller;

    /**
     * Apply the command
     */
    public void Apply() {}

    /**
     * Apply the command. Must be the exact reverse of Apply()
     */
    public void Reverse() {}

    /**
     * Constructor
     * @param controller the controller to apply the command
     */
    Command(MainWindowController controller) {
        this.controller = controller;
    }

    /**
     * Gets the controller  to apply the command
     * @return the controller  to apply the command
     */
    MainWindowController getController() {
        return controller;
    }
}
