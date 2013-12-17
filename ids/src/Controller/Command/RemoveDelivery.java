package Controller.Command;

import Controller.MainWindowController;
import Model.Delivery.Delivery;
import Model.Delivery.Round;

import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class RemoveDelivery extends InverseCommand {

    /**
     * Constructor
     * @param controller the controller to apply to
     * @param clientId the client's id
     * @param nodeId the node id to deliver to
     * @param earliestBound the earliest delivery bound
     * @param latestBound the latest delivery bound
     */
    public RemoveDelivery(MainWindowController controller, String clientId, int nodeId, GregorianCalendar earliestBound, GregorianCalendar latestBound) {
        super(new AddDelivery(controller, clientId, nodeId, earliestBound, latestBound));
    }

    /**
     * Creates a RemoveDelivery command for a given nodeId to remove
     * @param controller the controller to apply to
     * @param nodeId the node id to deliver to
     * @return a newly instantiated RemoveDelivery removing the delivery to nodeId
     */
    public static RemoveDelivery create(MainWindowController controller, int nodeId) {
        Round round = controller.getMainWindow().getRound();

        Delivery delivery = round.findDelivered(nodeId);

        String clientId = delivery.getClient().getId();
        GregorianCalendar earliestBound = delivery.getSchedule().getEarliestBound();
        GregorianCalendar latestBound = delivery.getSchedule().getLatestBound();

        return new RemoveDelivery(controller, clientId, nodeId, earliestBound, latestBound);
    }

}
