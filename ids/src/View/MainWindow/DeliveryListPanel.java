package View.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 03/12/13
 * Time: 20:20
 * Scrollable list of rounds on the left of the window
 */
public class DeliveryListPanel extends JScrollPane {

    private JList deliveryList = new JList();
    public static final float DEFAULT_WIDTH_RATIO = (float)0.2;
    public static final int PADDING = 6;

    public DeliveryListPanel() {

        // This array is just test data.
        // Of course we need to load data the controller gives us (fetched in the model).
        String[] testList = new String[4];
        testList[0] = "Mathieu";
        testList[1] = "Marian";
        testList[2] = "Christine";
        testList[3] = "Jeannine";

        // We bind the data structure to our JList and configure it
        deliveryList.setListData(testList);
        deliveryList.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        deliveryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        // We configure our JScrollPane
        setViewportView(deliveryList);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(new Dimension((int)(MainWindow.DEFAULT_WIDTH*DEFAULT_WIDTH_RATIO), 0));
    }
}
