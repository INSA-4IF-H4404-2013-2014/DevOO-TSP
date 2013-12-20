package controller.command;

import controller.MainWindowController;
import model.delivery.Round;

import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class AddDelivery extends Command {

    /** the client's id */
    String clientId;

    /** the node id to deliver to */
    int nodeId;

    /** the earliest delivery bound */
    GregorianCalendar earliestBound;

    /** the latest delivery bound */
    GregorianCalendar latestBound;

    /**
     * Constructor
     * @param controller the controller to apply to
     * @param clientId the client's id
     * @param nodeId the node id to deliver to
     * @param earliestBound the earliest delivery bound
     * @param latestBound the latest delivery bound
     */
    public AddDelivery(MainWindowController controller, String clientId, int nodeId, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        super(controller);
        this.clientId = clientId;
        this.nodeId = nodeId;
        this.earliestBound = earliestBound;
        this.latestBound = latestBound;
    }

    /**
     * implements command.Apply() to add a delivery
     */
    public void Apply() {
        Round round = this.getController().getMainWindow().getRound();

        round.addDelivery(clientId, nodeId, earliestBound, latestBound);
        this.controller.computeRound(controller.getMainWindow().getNetwork(),round);
    }

    /**
     * implements command.Reverse() to un-add/remove a delivery
     */
    public void Reverse() {
        Round round = this.getController().getMainWindow().getRound();

        round.removeDelivery(nodeId);
        this.controller.computeRound(controller.getMainWindow().getNetwork(),round);
    }

}
