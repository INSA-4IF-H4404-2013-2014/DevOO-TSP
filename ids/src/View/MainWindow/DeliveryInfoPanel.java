package View.MainWindow;

import View.TimeFramePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/12/13
 * Time: 21:15
 * In the main window, this is the right bottom part
 */
public class DeliveryInfoPanel extends JPanel {

    private JTextField client;
    private JTextField address;
    private JTextField deliveryDate;
    private JTextField delay;
    private TimeFramePanel timeFrame;

    public DeliveryInfoPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.LIGHT_GRAY);
        add(createDeliveryInfoPanelContent());
    }


    /**
     * Creates the dialog of the bottom delivery info panel.
     * @return the JPanel
     */
    private JPanel createDeliveryInfoPanelContent() {
        JPanel deliveryInfoPanelContent = new JPanel(new BorderLayout());
        deliveryInfoPanelContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        deliveryInfoPanelContent.add(new JLabel("Livraison"), BorderLayout.PAGE_START);

        // We create here the small "form"
        JPanel deliveryInfoPanelContentForm = new JPanel(new GridLayout(5, 1));

        // First row
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("ID : "));
        row1.add(new JTextField("", 4));

        // Second row
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Plage horaire : "));
        row2.add(new JTextField("", 4));
        row2.add(new JTextField("", 4));

        // Third row
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Nom du client : "));
        row3.add(new JTextField("", 6));

        // Fourth row
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row4.add(new JLabel("Adresse : "));
        row4.add(new JTextField("", 14));

        // Fifth row
        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row5.add(new JLabel("Arrivée : "));
        row5.add(new JTextField("", 4));
        row5.add(new JLabel("Retard : "));
        row5.add(new JTextField("", 4));

        deliveryInfoPanelContentForm.add(row1);
        deliveryInfoPanelContentForm.add(row2);
        deliveryInfoPanelContentForm.add(row3);
        deliveryInfoPanelContentForm.add(row4);
        deliveryInfoPanelContentForm.add(row5);

        // We add this "form" to topDeliveryInfoPanelContent
        deliveryInfoPanelContent.add(deliveryInfoPanelContentForm, BorderLayout.CENTER);

        return deliveryInfoPanelContent;
    }
}
