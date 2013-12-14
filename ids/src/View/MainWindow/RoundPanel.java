package View.MainWindow;

import Model.ChocoSolver.CalculatedRound;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 07/12/13
 * Time: 14:07
 * In the main window, this is the right top part
 */
public class RoundPanel extends JPanel {

    private JTextField deliveryCount = new JTextField("", 4);
    private JTextField duration = new JTextField("", 4);
    private JTextField delay = new JTextField("", 4);
    private JTextField distance = new JTextField("", 4);

    public RoundPanel() {
        final int borderSize = 5;
        final int rowHeight = 30;
        final int alignForms = 130 + 2 * borderSize;
        final int textFieldOffset = -5;

        JLabel labelCount = new JLabel("Nombre livraisons :");
        JLabel labelDuration = new JLabel("Durée :");
        JLabel labelDelay = new JLabel("Retard cumulé :");
        JLabel labelDistance = new JLabel("Longueur :");

        deliveryCount = new JTextField("", 4);
        duration = new JTextField("", 4);
        delay = new JTextField("", 4);
        distance = new JTextField("", 4);

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "Informations de la tounrée");
        setBorder(title);

        add(labelCount);
        add(labelDuration);
        add(labelDelay);
        add(labelDistance);

        add(deliveryCount);
        add(duration);
        add(delay);
        add(distance);

        layout.putConstraint(SpringLayout.WEST, deliveryCount, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, duration, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, delay, alignForms, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, distance, alignForms, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.EAST, deliveryCount, -borderSize, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, duration, -borderSize, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, delay, -borderSize, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.EAST, distance, -borderSize, SpringLayout.EAST, this);

        layout.putConstraint(SpringLayout.NORTH, deliveryCount, textFieldOffset, SpringLayout.NORTH, labelCount);
        layout.putConstraint(SpringLayout.NORTH, duration, textFieldOffset, SpringLayout.NORTH, labelDuration);
        layout.putConstraint(SpringLayout.NORTH, delay, textFieldOffset, SpringLayout.NORTH, labelDelay);
        layout.putConstraint(SpringLayout.NORTH, distance, textFieldOffset, SpringLayout.NORTH, labelDistance);

        layout.putConstraint(SpringLayout.EAST, labelCount, -borderSize, SpringLayout.WEST, deliveryCount);
        layout.putConstraint(SpringLayout.EAST, labelDuration, -borderSize, SpringLayout.WEST, duration);
        layout.putConstraint(SpringLayout.EAST, labelDelay, -borderSize, SpringLayout.WEST, delay);
        layout.putConstraint(SpringLayout.EAST, labelDistance, -borderSize, SpringLayout.WEST, distance);

        layout.putConstraint(SpringLayout.NORTH, labelCount, borderSize, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, labelDuration, rowHeight, SpringLayout.NORTH, labelCount);
        layout.putConstraint(SpringLayout.NORTH, labelDelay, rowHeight, SpringLayout.NORTH, labelDuration);
        layout.putConstraint(SpringLayout.NORTH, labelDistance, rowHeight, SpringLayout.NORTH, labelDelay);

        setEnableAllFields(false);
    }

    /**
     * Gets the delivery count field
     * @return the delivery count field
     */
    public JTextField getDeliveryCount() {
        return deliveryCount;
    }

    /**
     * Gets the duration field
     * @return the duration field
     */
    public JTextField getDuration() {
        return duration;
    }

    /**
     * Gets the delay field
     * @return the delay field
     */
    public JTextField getDelay() {
        return delay;
    }

    /**
     * Gets the distance field
     * @return the distance field
     */
    public JTextField getDistance() {
        return distance;
    }

    /**
     * Enable or disable the fields
     * @param b whether to enable or disable the fields
     */
    private void setEnableAllFields(boolean b) {
        View.Utils.enableJTextField(deliveryCount, b);
        View.Utils.enableJTextField(duration, b);
        View.Utils.enableJTextField(delay, b);
        View.Utils.enableJTextField(distance, b);
    }

    /**
     * fill all the fields with the information in the Delivery given
     * @param round the round which should be described
     */
    public void fillRoundPanel(CalculatedRound round){
        if(round != null)
        {
            // TODO: There is a bug here!
            /*
            this.delay.setText(CalculatedRound.conversionMSHM(round.getCumulatedDelay()));
            this.distance.setText(""+round.getTotalLength());
            // -1 because the warehouse is two times in this list
            this.deliveryCount.setText(""+(round.getOrderedItineraries().size()-1));
            this.duration.setText(CalculatedRound.conversionMSHM(round.getTotalDuration()));
            */
        }
    }

    /**
     * empty all the fields
     */
    public void emptyFields(){
        this.delay.setText("");
        this.distance.setText("");
        this.deliveryCount.setText("");
        this.duration.setText("");
    }

}
