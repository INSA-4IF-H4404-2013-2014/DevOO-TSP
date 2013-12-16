package Tests.City;

import Model.City.*;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 11/12/13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class NetworkTest {

    /**
     *  Nodes position
     *      N H
     *    W C E
     *      S
     */

    private Network network;

    private Node C;
    private Node N;
    private Node E;
    private Node S;
    private Node W;
    private Node H;

    private Arc NC;
    private Arc CN;

    private Arc SC;
    private Arc CS;

    private Arc EC;
    private Arc CE;

    private Arc WC;
    private Arc CW;

    private Arc HC;
    private Arc CH;


    public NetworkTest() {
        network = new Network();

        C = network.createNode(0, 0, 0);
        N = network.createNode(1, 0, 10);
        E = network.createNode(2, 10, 0);
        S = network.createNode(3, 0, -10);
        W = network.createNode(4, -10, 0);
        H = network.createNode(5, 10, 10);

        NC = network.createStreet("NS").createArc(N, C, 10.0f, 1.0f);
        CN = network.createStreet("NS").createArc(C, N, 10.0f, 1.0f);

        SC = network.createStreet("NS").createArc(S, C, 10.0f, 1.0f);
        CS = network.createStreet("NS").createArc(C, S, 10.0f, 1.0f);

        EC = network.createStreet("EW").createArc(E, C, 10.0f, 1.0f);
        CE = network.createStreet("EW").createArc(C, E, 10.0f, 1.0f);

        WC = network.createStreet("EW").createArc(W, C, 10.0f, 1.0f);
        CW = network.createStreet("EW").createArc(C, W, 10.0f, 1.0f);

        HC = network.createStreet("H").createArc(H, C, 10.0f, 1.0f);
        CH = network.createStreet("H").createArc(C, H, 10.0f, 1.0f);
    }

    /**
     * Checks if findOutgoingTo finds the right street
     */
    @Test
    public void testNodeFindOutgoingTo() {
        assertTrue(CW == C.findOutgoingTo(W));
        assertTrue(CE == C.findOutgoingTo(2));
    }

    /**
     * Checks if getDirectionAngle gets the right angle
     */
    @Test
    public void testArcDirectionAngle() {
        assertTrue(Math.abs(CN.getDirectionAngle() - 0.0) < 0.01);
        assertTrue(Math.abs(CH.getDirectionAngle() - 45.0) < 0.01);
        assertTrue(Math.abs(CE.getDirectionAngle() - 90.0) < 0.01);
        assertTrue(Math.abs(CS.getDirectionAngle() - 180.0) < 0.01);
        assertTrue(Math.abs(CW.getDirectionAngle() - 270.0) < 0.01);
    }

    /**
     * Checks if getDirectionTo gets the right direction
     */
    @Test
    public void testGetDirectionTo() {

        Arc.Direction direction1 = NC.getDirectionTo(CS);
        Arc.Direction direction2 = NC.getDirectionTo(CN);
        Arc.Direction direction3 = NC.getDirectionTo(CH);
        Arc.Direction direction4 = SC.getDirectionTo(CE);
        Arc.Direction direction5 = SC.getDirectionTo(CW);

        assertTrue(direction1 == Arc.Direction.KEEP_GOING);
        assertTrue(direction2 == Arc.Direction.TURN_BACK);
        assertTrue(direction3 == Arc.Direction.GO_ON);
        assertTrue(direction4 == Arc.Direction.TURN_RIGHT);
        assertTrue(direction5 == Arc.Direction.TURN_LEFT);

    }

}
