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
    /**
     * the formular to add a client
     */
    private DeliveryDialog dialog;
    /**
     * begin of the schedule
     */
    private GregorianCalendar begin;
    /**
     * end of the schedule
     */
    private GregorianCalendar end;
    /**
     * id (already here or new) of the client
     */
    private String client;
    /**
     * id of the node selected
     */
    private int address;
    /**
     * tells whether or not all the necessary fields are complete and right
     */
    private boolean addReady;

    /**
     * hour of begin
     */
    private int beginH;
    /**
     * minute of begin
     */
    private int beginM;
    /**
     * hour of end
     */
    private int endH;
    /**
     * minutes of end
     */
    private int endM;


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
        String dateTxt;

        try{
            if(dialog.getTimeFrameBeginH().getText().equals("")){
                beginH = 0;
            }else{
                beginH = Integer.parseInt(dialog.getTimeFrameBeginH().getText());
            }
            if(dialog.getTimeFrameBeginM().getText().equals("")){
                beginM = 0;
            }else{
                beginM = Integer.parseInt(dialog.getTimeFrameBeginM().getText());
            }

            if ((beginH <0 ) || (beginM <0)||(beginH > 23) || (beginM > 59)){
                throw new Exception();
            }
            dateTxt ="" + beginH + ":" +beginM;
            parsed = df.parse(dateTxt);
        }catch(Exception e){
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "veuillez entrer une heure entre 0 et 23h et des minutes en 0 et 59",
                    "Erreur horaire debut", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        GregorianCalendar cal = new GregorianCalendar();
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
        String dateTxt;
        try{
            if(dialog.getTimeFrameEndH().getText().equals("")){
                endH = 0;
            }else{
                endH = Integer.parseInt(dialog.getTimeFrameEndH().getText());
            }
            if(dialog.getTimeFrameEndM().getText().equals("")){
                endM = 0;
            }else{
                endM = Integer.parseInt(dialog.getTimeFrameEndM().getText());
            }

            if ((endH <0 ) || (endM <0)||(endH > 23) || (endM > 59)){
                throw new Exception();
            }
        }catch(Exception e){
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "veuillez entrer une heure entre 0 et 23h et des minutes en 0 et 59",
                    "Erreur horaire fin", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try{
            if((endH < beginH) || ((endH == beginH)&&(endM <= beginM))){
                throw new Exception();
            }
            dateTxt ="" + endH + ":" +endM;
            parsed = df.parse(dateTxt);
        } catch (Exception e) {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "fin de plage horaire avant le debut", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
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
