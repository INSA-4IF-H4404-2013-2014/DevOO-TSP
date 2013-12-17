package Controller.Command;

import Controller.MainWindowController;
import Model.Delivery.Delivery;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class RemoveDelivery extends InverseCommand {

    public RemoveDelivery(MainWindowController controller, Delivery delivery) {
        super(new AddDelivery(controller, delivery));
    }
}
