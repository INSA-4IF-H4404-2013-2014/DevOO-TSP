package Controller.Command;

import Controller.MainWindowController;
import Model.Delivery.Delivery;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class AddDelivery extends Command {

    Delivery delivery;

    public AddDelivery(MainWindowController controller, Delivery delivery) {
        super(controller);
        this.delivery = delivery;
    }

    public void Apply() {
        //TODO: add delivery!
    }

    public void Reverse() {
        //TODO: remove delivery!
    }

}
