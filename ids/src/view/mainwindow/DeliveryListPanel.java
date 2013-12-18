package view.mainwindow;

import controller.MainWindowController;
import model.delivery.Delivery;
import view.Utils;

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

    /** JList view associated with this panel */
    private JList deliveryList = new JList();

    /** Ratio of the full width this panel will take */
    public static final float DEFAULT_WIDTH_RATIO = (float)0.24;

    /** Padding, so that this panel can breathe. :-) */
    public static final int PADDING = 6;

    /**
     * Constructor
     */
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
