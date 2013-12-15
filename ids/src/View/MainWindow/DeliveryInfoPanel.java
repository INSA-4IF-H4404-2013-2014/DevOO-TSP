package View.MainWindow;

import Model.ChocoSolver.CalculatedRound;
import Model.City.Node;
import Model.Delivery.Delivery;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/12/13
 * Time: 21:15
 * In the main window, this is the right bottom part
 */
public class DeliveryInfoPanel extends JPanel {

    private JTextField deliveryID;
    private JTextField timeFrameBegin;
    private JTextField timeFrameEnd;
    private JTextField clientName;
    private JTextField address;
    private JTextField deliveryTime;
    private JTextField delay;

    /**
     * Constructor
     */
    public DeliveryInfoPanel() {
        final int borderSize = 5;
        final int rowHeight = 30;
        final int alignForms = 130 + 2 * borderSize;
        final int textFieldOffset = -5;

        JLabel labelId = new JLabel("ID :");
        JLabel labelTimeFrame = new JLabel("Plage horaire :");
        JLabel labelTimeFrameTo = new JLabel("à");
        JLabel labelClient = new JLabel("Nom du client :");
        JLabel labelAddress = new JLabel("Adresse :");
        JLabel labelDeliveryTime = new JLabel("Arrivée :");
        JLabel labelDeliveryDelay = new JLabel("Retard :");

        deliveryID = new JTextField("", 4);
        timeFrameBegin = new JTextField("", 4);
        timeFrameEnd = new JTextField("", 4);
        clientName = new JTextField("", 10);
        address = new JTextField("", 14);
        deliveryTime = new JTextField("", 4);
        delay = new JTextField("", 4);

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "Informations de livraison");
        setBorder(title);

        add(labelId);
        add(labelTimeFrame);
        add(labelTimeFrameTo);
        add(labelClient);
        add(labelAddress);
        add(labelDeliveryTime);
        add(labelDeliveryDelay);

        add(deliveryID);
        add(timeFrameBegin);
        add(timeFrameEnd);
        add(clientName);
        add(address);
        add(deliveryTime);
        add(delay);

        layout.putConstraint(SpringLayout.WEST, deliveryID, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, timeFrameBegin, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, clientName, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, address, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, deliveryTime, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, delay, alignForms, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, labelTimeFrameTo, 0, SpringLayout.NORTH, labelTimeFrame);
        layout.putConstraint(SpringLayout.WEST, labelTimeFrameTo, borderSize, SpringLayout.EAST, timeFrameBegin);
        layout.putConstraint(SpringLayout.WEST, timeFrameEnd, borderSize, SpringLayout.EAST, labelTimeFrameTo);

        layout.putConstraint(SpringLayout.EAST, deliveryID, -borderSize, SpringLayout.EAST, this);
        //layout.putConstraint(SpringLayout.EAST, timeFrameEnd, -borderSize, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, clientName, -borderSize, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, address, -borderSize, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, deliveryTime, -borderSize, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, delay, -borderSize, SpringLayout.EAST, this);

        layout.putConstraint(SpringLayout.NORTH, deliveryID, textFieldOffset, SpringLayout.NORTH, labelId);
        layout.putConstraint(SpringLayout.NORTH, timeFrameBegin, textFieldOffset, SpringLayout.NORTH, labelTimeFrame);
        layout.putConstraint(SpringLayout.NORTH, timeFrameEnd, textFieldOffset, SpringLayout.NORTH, labelTimeFrame);
        layout.putConstraint(SpringLayout.NORTH, clientName, textFieldOffset, SpringLayout.NORTH, labelClient);
        layout.putConstraint(SpringLayout.NORTH, address, textFieldOffset, SpringLayout.NORTH, labelAddress);
        layout.putConstraint(SpringLayout.NORTH, deliveryTime, textFieldOffset, SpringLayout.NORTH, labelDeliveryTime);
        layout.putConstraint(SpringLayout.NORTH, delay, textFieldOffset, SpringLayout.NORTH, labelDeliveryDelay);

        layout.putConstraint(SpringLayout.EAST, labelId, -borderSize, SpringLayout.WEST, deliveryID);
        layout.putConstraint(SpringLayout.EAST, labelTimeFrame, -borderSize, SpringLayout.WEST, timeFrameBegin);
        layout.putConstraint(SpringLayout.EAST, labelClient, -borderSize, SpringLayout.WEST, clientName);
        layout.putConstraint(SpringLayout.EAST, labelAddress, -borderSize, SpringLayout.WEST, address);
        layout.putConstraint(SpringLayout.EAST, labelDeliveryTime, -borderSize, SpringLayout.WEST, deliveryTime);
        layout.putConstraint(SpringLayout.EAST, labelDeliveryDelay, -borderSize, SpringLayout.WEST, delay);

        layout.putConstraint(SpringLayout.NORTH, labelId, borderSize, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, labelTimeFrame, rowHeight, SpringLayout.NORTH, labelId);
        layout.putConstraint(SpringLayout.NORTH, labelClient, rowHeight, SpringLayout.NORTH, labelTimeFrame);
        layout.putConstraint(SpringLayout.NORTH, labelAddress, rowHeight, SpringLayout.NORTH, labelClient);
        layout.putConstraint(SpringLayout.NORTH, labelDeliveryTime, rowHeight, SpringLayout.NORTH, labelAddress);
        layout.putConstraint(SpringLayout.NORTH, labelDeliveryDelay, rowHeight, SpringLayout.NORTH, labelDeliveryTime);

        setEnableAllFields(false);
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
        SimpleDateFormat form = new SimpleDateFormat("kk:mm");
        timeFrameBegin.setText(""+form.format(delivery.getSchedule().getEarliestBound().getTime()));
        timeFrameEnd.setText(""+form.format(delivery.getSchedule().getLatestBound().getTime()));
         if(round != null)
         {
            deliveryTime.setText(""+form.format(round.getEstimatedSchedules(delivery.getAddress().getId()).getTime()));
            delay.setText(""+round.getDelay(delivery.getAddress().getId()));
         }
     }

    /**
     * Fill the fields with the required node informations
     * @param node The node (which is not a delivery node)
     */
    public void fillDeliveryInfoPanelNonDeliveryNode(Node node) {
        address.setText(String.valueOf(node.getId()));
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
