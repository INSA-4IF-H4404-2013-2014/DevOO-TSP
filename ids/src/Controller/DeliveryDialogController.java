package Controller;

import View.DeliveryDialog;
import View.MainWindow.MainWindow;

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
    public DeliveryDialogController(MainWindow mainWindow) {
        dialog = new DeliveryDialog(this, mainWindow);
    }

    /**
     * Action to be performed when the user clicks on "confirm" button
     */
    public void confirm() {
        //TODO trnasform text into real structures
        //TODO close the dialog if ok
        //TODO error boxes ( wrong format, id already set?, etc.)
        if (checkAnsOk())      {
            System.out.println(dialog.getParent().getMapPanel().getSelectedNode().toString());
            if( dialog.newCliIsSelected()) {
                System.out.println(dialog.getNewClient().getText());
            }
            else {
                System.out.println(dialog.getClientBox().getSelectedItem().toString());
            }
            System.out.println(dialog.getTimeFrameBegin().getText());
            System.out.println(dialog.getTimeFrameEnd().getText());
            dialog.dispose();
        }
    }

    /**
     * check if all the fields have correctly been filled
     * @return true if everything ok, false and popup messages if not
     */
    public boolean checkAnsOk(){
        boolean allIsOk = checkNewCliOk();
        if (allIsOk){
            allIsOk = checkScheduleOk();
        }
        return allIsOk;
    }


    /**
     * check if all the fields have correctly been filled
     * @return true if everything ok, false and popup messages if not
     */
    public boolean checkNewCliOk(){
        boolean allIsOk = true;
        try {
            if(dialog.newCliIsSelected()){
                int testInt =  Integer.parseInt(dialog.getNewClient().getText());
                if(testInt == 0)
                {
                    allIsOk=false;
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "l'id 0 n'est pas disponible pour un client", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                if(dialog.getParent().getRound().getIndexClient( dialog.getNewClient().getText()) != -1 ){
                    allIsOk=false;
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "le numéro d'id du client que vous voulez ajouter existe déjà", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch (Exception e){
            allIsOk=false;
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Mauvais format de donnée dans le champ nouveau client", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return allIsOk;
    }

    public boolean checkScheduleOk(){
        boolean allIsOk = true;
        try {
            if(false ){
                //TODO debut apres fin
                allIsOk=false;
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "debut de plage horaire apres sa fin", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }catch (Exception e){
            //TODO hours format acceptable or not (00h00 00:00 00)
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Mauvais format de donnée dans le(s) champ horaire", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return allIsOk;
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
