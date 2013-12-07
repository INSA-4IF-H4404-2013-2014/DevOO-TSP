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

        // We add this "form" to topDeliveryInfoPanelContent
        deliveryInfoPanelContent.add(deliveryInfoPanelContentForm, BorderLayout.CENTER);

        return deliveryInfoPanelContent;
    }

    private JPanel createRow1() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("ID : "));
        row.add(new JTextField("", 4));
        return row;
    }

    private JPanel createRow2() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Plage horaire : "));
        row.add(new JTextField("", 4));
        row.add(new JTextField("", 4));
        return row;
    }

    private JPanel createRow3() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Nom du client : "));
        row.add(new JTextField("", 6));
        return row;
    }

    private JPanel createRow4() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Adresse : "));
        row.add(new JTextField("", 14));
        return row;
    }

    private JPanel createRow5() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Arrivée : "));
        row.add(new JTextField("", 4));
        row.add(new JLabel("Retard : "));
        row.add(new JTextField("", 4));
        return row;
    }
}
