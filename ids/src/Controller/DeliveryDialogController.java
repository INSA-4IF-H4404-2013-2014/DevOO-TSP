package Controller;

import View.DeliveryDialog;

import javax.swing.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 10/12/13
 * Time: 20:14
 * This class is the controller of the small dialog window which is opened when adding a delivery.
 */
public class DeliveryDialogController {
    private DeliveryDialog dialog;

    /**
     * Creates the controller.
     * @param mainWindow: the parent window (for the modal mode)
     */
    public DeliveryDialogController(JFrame mainWindow) {
        dialog = new DeliveryDialog(this, mainWindow);

    }

    /**
     * Action to be performed when the user clicks on "confirm" button
     */
    public void confirm() {

    }

    /**
     * Action to be performed when the user clicks on "cancel" button
     */
    public void cancel() {
        dialog.dispose();
    }

    /**
     * Sets the dialog visible.
     *
     * This allows the setVisible(true) to be called from another class: not from our controller.
     * Calling setVisible(true) in our controller would cause the constructor to never finish!
     */
    protected void show() {
        dialog.setVisible(true);
    }
}
