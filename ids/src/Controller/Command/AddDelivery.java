package Controller.Command;

import Controller.MainWindowController;
import Model.Delivery.Delivery;
import Model.Delivery.Round;

import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class AddDelivery extends Command {

    String clientId;
    int nodeId;
    GregorianCalendar earliestBound;
    GregorianCalendar latestBound;

    public AddDelivery(MainWindowController controller, String clientId, int nodeId, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        super(controller);
        this.clientId = clientId;
        this.nodeId = nodeId;
        this.earliestBound = earliestBound;
        this.latestBound = latestBound;
    }

    public void Apply() {
        Round round = this.getController().getMainWindow().getRound();

        round.addDelivery(clientId, nodeId, earliestBound, latestBound);
        this.controller.computeRound(controller.getMainWindow().getNetwork(),round);
    }

    public void Reverse() {
        Round round = this.getController().getMainWindow().getRound();

        round.removeDelivery(nodeId);
        this.controller.computeRound(controller.getMainWindow().getNetwork(),round);
    }

}
