package View.MainWindow;

import Model.ChocoSolver.CalculatedRound;

import javax.swing.*;
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
        setLayout(new GridBagLayout());
        add(createRoundPanelContent());
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
     * Creates the dialog of the round panel.
     * @return the JPanel
     */
    private JPanel createRoundPanelContent() {
        JPanel roundPanelContent = new JPanel(new GridLayout(3, 1));
        roundPanelContent.setBorder
                (
                        new TitledBorder
                                (
                                        BorderFactory.createLineBorder(Color.BLACK, 1),
                                        "Tournée",
                                        TitledBorder.DEFAULT_JUSTIFICATION,
                                        TitledBorder.DEFAULT_POSITION,
                                        new Font("Verdana", Font.BOLD, 17)
                                )
                );
        roundPanelContent.add(createRow1());
        roundPanelContent.add(createRow2());
        roundPanelContent.add(createRow3());
        setEnableAllFields(false);

        return roundPanelContent;
    }

    private JPanel createRow1() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Nombre de livraisons : "));
        row.add(deliveryCount);
        return row;
    }

    private JPanel createRow2() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Durée : "));
        row.add(duration);
        row.add(new JLabel("Retard cumulé :"));
        row.add(delay);
        return row;
    }

    private JPanel createRow3() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Longueur : "));
        row.add(distance);
        return row;
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
