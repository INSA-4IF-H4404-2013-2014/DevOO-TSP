package Tests.ChocoSolver;

import Model.City.Network;
import Model.City.Node;
import Model.Delivery.Client;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: nicolas
 * Date: 11/12/13
 * Time: 09:21
 * To change this template use File | Settings | File Templates.
 */
public class CalculatedRoundTest {

    @Test
    public void testEstimatedSchedule() {
        // TODO: test this function once computeRound() is ready

        Network myNetwork = new Network();

        myNetwork.createNode(1, 5, 5);
        myNetwork.createNode(2, 21, 10);
        myNetwork.createNode(3, 30, 60);

        myNetwork.createStreet("1");
        myNetwork.createStreet("2");
        myNetwork.createStreet("3");

        myNetwork.findStreet("1").createArc(myNetwork.findNode(1), myNetwork.findNode(2), 10, 100);
        myNetwork.findStreet("2").createArc(myNetwork.findNode(2), myNetwork.findNode(3), 10, 200);
        myNetwork.findStreet("3").createArc(myNetwork.findNode(3), myNetwork.findNode(1), 10, 100);

        Client client = new Client("Toto");

    }
}
