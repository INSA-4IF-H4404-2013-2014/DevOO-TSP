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

    Controller.MainWindowController controller;

    void Apply() {}

    void Reverse() {}

    Command(MainWindowController controller) {
        this.controller = controller;
    }

    MainWindowController getController() {
        return controller;
    }
}
