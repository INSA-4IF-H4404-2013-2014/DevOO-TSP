package View.MainWindow;

import Model.ChocoSolver.CalculatedRound;
import Model.Delivery.Delivery;
import Model.Delivery.Round;
import View.TimeFramePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/12/13
 * Time: 21:15
 * In the main window, this is the right bottom part
 */
public class DeliveryInfoPanel extends JPanel {

    private JTextField deliveryID = new JTextField("", 4);
    private JTextField timeFrameBegin = new JTextField("", 4);
    private JTextField timeFrameEnd = new JTextField("", 4);
    private JTextField clientName = new JTextField("", 10);
    private JTextField address = new JTextField("", 14);
    private JTextField deliveryTime = new JTextField("", 4);
    private JTextField delay = new JTextField("", 4);

    public DeliveryInfoPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.LIGHT_GRAY);
        add(createDeliveryInfoPanelContent());
    }

    /**
     * Creates the dialog of the delivery info panel.
     * @return the JPanel
     */
    private JPanel createDeliveryInfoPanelContent() {
        JPanel deliveryInfoPanelContent = new JPanel(new BorderLayout());
        deliveryInfoPanelContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        deliveryInfoPanelContent.add(new JLabel("Livraison"), BorderLayout.PAGE_START);

        // We create here the small "form"
        JPanel deliveryInfoPanelContentForm = new JPanel(new GridLayout(5, 1));
        deliveryInfoPanelContentForm.add(createRow1());
        deliveryInfoPanelContentForm.add(createRow2());
        deliveryInfoPanelContentForm.add(createRow3());
        deliveryInfoPanelContentForm.add(createRow4());
        deliveryInfoPanelContentForm.add(createRow5());
        setEnableAllFields(false);

        // We add this "form" to deliveryInfoPanelContent
        deliveryInfoPanelContent.add(deliveryInfoPanelContentForm, BorderLayout.CENTER);

        return deliveryInfoPanelContent;
    }

    private JPanel createRow1() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("ID : "));
        row.add(deliveryID);
        return row;
    }

    private JPanel createRow2() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Plage horaire : "));
        row.add(timeFrameBegin);
        row.add(timeFrameEnd);
        return row;
    }

    private JPanel createRow3() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Nom du client : "));
        row.add(clientName);
        return row;
    }

    private JPanel createRow4() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Adresse : "));
        row.add(address);
        return row;
    }

    private JPanel createRow5() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Arrivée : "));
        row.add(deliveryTime);
        row.add(new JLabel("Retard : "));
        row.add(delay);
        return row;
    }

    /**
     * Enable or disable the fields
     * @param b whether to enable or disable the fields
     */
    private void setEnableAllFields(boolean b) {
        View.Utils.enableJTextField(deliveryID, b);
        View.Utils.enableJTextField(timeFrameBegin, b);
        View.Utils.enableJTextField(timeFrameEnd, b);
        View.Utils.enableJTextField(clientName, b);
        View.Utils.enableJTextField(address, b);
        View.Utils.enableJTextField(deliveryTime, b);
        View.Utils.enableJTextField(delay, b);
    }

    /**
     * fill all the fields with the information in the Delivery given
     * @param delivery the delivery which should be described
     */
     public void fillDeliveryInfoPanel(Delivery delivery, CalculatedRound round){

            deliveryID.setText(""+delivery.getId());
            clientName.setText(""+delivery.getClient().getId());
            address.setText((""+delivery.getAddress().getId()));
            SimpleDateFormat form = new SimpleDateFormat("hh:mm");
            timeFrameBegin.setText(""+form.format(delivery.getSchedule().getEarliestBound().getTime()));
            timeFrameEnd.setText(""+form.format(delivery.getSchedule().getLatestBound().getTime()));
         if(round != null)
         {
            deliveryTime.setText(""+form.format(round.getEstimatedSchedules(delivery.getAddress().getId()).getTime()));
            delay.setText(""+round.getDelay(delivery.getId()));
         }
     }

    /**
    * empty all the fields
    */
    public void emptyFields(){
        deliveryID.setText("");
        clientName.setText("");
        address.setText("");
        timeFrameBegin.setText("");
        timeFrameEnd.setText("");
        deliveryTime.setText("");
        delay.setText("");
    }

    /**
     * empty the fields  concerned by a round
     */
    public void emptyRoundFields(){
        deliveryTime.setText("");
        delay.setText("");
    }

}
