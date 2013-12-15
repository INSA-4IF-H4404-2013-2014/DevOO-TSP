package Controller;

import Model.City.Node;
import Model.Delivery.Client;
import Model.Delivery.Delivery;
import Model.Delivery.Schedule;
import View.DeliveryDialog;
import View.MainWindow.MainWindow;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 10/12/13
 * Time: 20:14
 * This class is the controller of the small dialog window which is opened when adding a delivery.
 */
public class DeliveryDialogController {
    private DeliveryDialog dialog;
    private GregorianCalendar begin;
    private GregorianCalendar end;
    private String client;
    private int address;
    private boolean addReady;

    /**
     * Creates the controller.
     * @param mainWindow: the parent window (for the modal mode)
     */
    public DeliveryDialogController(MainWindow mainWindow) {
        dialog = new DeliveryDialog(this, mainWindow);
        addReady = false;
    }


    /**
     * get the begining of the schedule
     * @return the begining of the schedule
     */
    public GregorianCalendar getBegin() {
        return begin;
    }

    /**
     * get the end of the schedule
     * @return the end of the schedule
     */
    public GregorianCalendar getEnd() {
        return end;
    }

    /**
     * get the end of the schedule
     * @return the end of the schedule
     */
    public String getClient() {
        return client;
    }

    /**
     * get the address of the delivery
     * @return the node where the delivery has to be delivered
     */
    public int getAddress() {
        return address;
    }

    /**
     * tell if the dialog have all his informations ok
     * @return true if all the informations permit to create a delivery
     */
    public boolean isAddReady() {
        return addReady;
    }

    /**
     * Action to be performed when the user clicks on "confirm" button
     */
    public void confirm(){
        if (checkAnsOk()&&(begin != null)&&(end != null)){
            try{
                Schedule schedule = new Schedule(begin, end);
                int id = dialog.getParent().getRound().findAnId();
                address = dialog.getParent().getMapPanel().getSelectedNode().getId();
                dialog.dispose();
                this.addReady =true;
            }catch(Exception e){
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "problème lors de la création de la livraison", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * method which tell if all the fields are ok or not
     * @return if the informations are complete
     */
    public boolean addIsReady(){
        return addReady;
    }


    /**
     * check if all the fields have correctly been filled
     * @return true if everything ok, false and popup messages if not
     */
    private boolean checkAnsOk(){
        boolean allIsOk = checkCliOk();
        if (allIsOk){
            allIsOk = checkBeginField();
            if (allIsOk){
                allIsOk = checkEndField();
            }
        }
        return allIsOk;
    }


    /**
     * check if all the fields have correctly been filled in the client part
     * and fill the client attribute with the relevant information
     * @return true if everything ok, false and popup messages if not
     */
    private boolean checkCliOk(){
        try {
            //the problems can happen when creating a new client
            if(dialog.newCliIsSelected()){
                int testInt =  Integer.parseInt(dialog.getNewClient().getText());
                if(testInt == 0)
                {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "l'id 0 n'est pas disponible pour un client", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(dialog.getParent().getRound().getIndexClient( dialog.getNewClient().getText()) != -1 ){
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "le numéro d'id du client que vous voulez ajouter existe déjà", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                client = dialog.getNewClient().getText();
            }
            else {
                client =((Client)dialog.getClientBox().getSelectedItem()).getId();
            }
        }catch (Exception e){
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Mauvais format de donnée dans le champ nouveau client", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * check if all the fields have correctly been filled in the schedule begin part
     * and fill the begin attribute with the relevant information
     * @return true if everything ok, false and popup messages if not
     */
    private boolean checkBeginField(){
        DateFormat df = new SimpleDateFormat("kk:mm");
        Date parsed = new Date();
        try {
            parsed = df.parse(dialog.getTimeFrameBegin().getText());
        }catch (ParseException e){
            df = new SimpleDateFormat("kk");
            try{
                parsed = df.parse(dialog.getTimeFrameBegin().getText());
            }catch(ParseException ex){
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "Mauvais format de donnée dans le(s) champ begin", "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        GregorianCalendar cal = new GregorianCalendar();//parsed.toGregorianCalendar();
        cal.setTime(parsed);
        begin = cal;
        return true;
    }

    /**
     * check if all the fields have correctly been filled in the schedule end part
     * and fill the end attribute with the relevant information
     * @return true if everything ok, false and popup messages if not
     */
    private boolean checkEndField(){
        DateFormat df = new SimpleDateFormat("hh:mm");
        Date parsed;
        try {
            parsed = df.parse(dialog.getTimeFrameEnd().getText());
        }catch (ParseException e){
            df = new SimpleDateFormat("hh");
            try{
                parsed = df.parse(dialog.getTimeFrameEnd().getText());
            }catch(ParseException ex){
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "Mauvais format de donnée dans le(s) champ begin", "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        GregorianCalendar cal = new GregorianCalendar();//parsed.toGregorianCalendar();
        cal.setTime(parsed);
        end = cal;
        return true;
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
