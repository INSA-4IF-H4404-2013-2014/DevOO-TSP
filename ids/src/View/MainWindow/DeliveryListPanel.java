package View.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 03/12/13
 * Time: 20:20
 * Scrollable list of rounds on the left of the window
 */
public class DeliveryListPanel extends JScrollPane {

    private JList deliveryList;

    public DeliveryListPanel() {

        // This array is just test data.
        // Of course we need to load data the controller gives us (fetched in the model).
        String[] testList = new String[4];
        testList[0] = "Mathieu";
        testList[1] = "Marian";
        testList[2] = "Christine";
        testList[3] = "Jeannine";

        // We bind the data structure to our JList and configure it
        deliveryList = new JList(testList);


        // We configure our JScrollPane
        setViewportView(deliveryList);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(new Dimension(200, 0));
    }
}
