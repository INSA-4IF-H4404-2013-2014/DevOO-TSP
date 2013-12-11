package View.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;

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
        deliveryList.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        deliveryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deliveryList.setEnabled(false);


        // We configure our JScrollPane
        setViewportView(deliveryList);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(new Dimension((int)(MainWindow.DEFAULT_WIDTH*DEFAULT_WIDTH_RATIO), 0));
    }

    /**
     * Asks the view to display the given list
     * @param list to display
     */
    public void setModel(Vector<String> list) {
        deliveryList.setListData(list);
        deliveryList.setEnabled(true);
    }
}
