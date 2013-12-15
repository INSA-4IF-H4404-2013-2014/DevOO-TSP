package View.MainWindow;

import Controller.MainWindowController;
import Model.Delivery.Delivery;
import View.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    public static final float DEFAULT_WIDTH_RATIO = (float)0.24;
    public static final int PADDING = 6;

    public DeliveryListPanel() {
        deliveryList.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        deliveryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Utils.enableJList(deliveryList, false);

        // We configure our JScrollPane
        setViewportView(deliveryList);
        setPreferredSize(new Dimension((int) (MainWindow.DEFAULT_WIDTH * DEFAULT_WIDTH_RATIO), 0));
    }

    /**
     * Asks the view to display the given list
     * @param list to display
     */
    public void setModel(Vector<Delivery> list) {
        deliveryList.setListData(list);
        Utils.enableJList(deliveryList, list.size() > 0);
    }

    /**
     * Adds the listener
     * @param controller controller which will listen to the event
     */
    public void addListener(final MainWindowController controller) {
        deliveryList.getSelectionModel().addListSelectionListener(controller);
    }

    /**
     * Gets the currently selected delivery in the deliveryList.
     * @return the currently selected delivery
     */
    public Delivery getSelectedValue() {
        return (Delivery)deliveryList.getSelectedValue();
    }

    /**
     * Sets the currently selected delivery in the deliveryList.
     * @param delivery The delivery to select.
     */
    public void setSelectedValue(Delivery delivery) {
        deliveryList.setSelectedValue(delivery, true);
    }

    /**
     * Clears the selection in the deliveryList
     */
    public void clearSelection() {
        deliveryList.clearSelection();
    }
}
