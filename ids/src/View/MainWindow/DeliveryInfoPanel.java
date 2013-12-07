package View.MainWindow;

import View.TimeFramePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/12/13
 * Time: 21:15
 * This is the right side of the main window
 */
public class DeliveryInfoPanel extends JPanel {

    public static final float DEFAULT_WIDTH_RATIO = (float)0.33;

    private JTextField client;
    private JTextField address;
    private JTextField deliveryDate;
    private JTextField delay;
    private TimeFramePanel timeFrame;

    public DeliveryInfoPanel() {
        setPreferredSize(new Dimension((int)(MainWindow.DEFAULT_WIDTH*DEFAULT_WIDTH_RATIO), 0));

        setLayout(new GridLayout(2,1));

        add(createTopDeliveryInfoPanel());
        add(createBottomDeliveryInfoPanel());
    }

    /**
     * Creates top part of the delivery info panel (this is the top most right part
     * in the main window).
     *
     * @return the JPanel
     */
    private JPanel createTopDeliveryInfoPanel() {
        JPanel topDeliveryInfoPanel = new JPanel(new GridBagLayout());
        topDeliveryInfoPanel.setBackground(Color.GRAY);
        topDeliveryInfoPanel.add(createTopDeliveryInfoPanelContent());
        return topDeliveryInfoPanel;
    }

    /**
     * Creates the bottom part of the delivery info panel (this is the bottom most right part
     * in the main window).
     * @return
     */
    private JPanel createBottomDeliveryInfoPanel() {
        JPanel bottomDeliveryInfoPanel = new JPanel(new GridBagLayout());
        bottomDeliveryInfoPanel.setBackground(Color.LIGHT_GRAY);
        bottomDeliveryInfoPanel.add(createBottomDeliveryInfoPanelContent());
        return bottomDeliveryInfoPanel;
    }


    /**
     * Creates the dialog of the top delivery info panel.
     * @return the JPanel
     */
    private JPanel createTopDeliveryInfoPanelContent() {
        JPanel topDeliveryInfoPanelContent = new JPanel(new BorderLayout());
        topDeliveryInfoPanelContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        topDeliveryInfoPanelContent.add(new JLabel("Tournée"), BorderLayout.PAGE_START);

        // We create here the small "form"
        JPanel topDeliveryInfoPanelContentForm = new JPanel(new GridLayout(3, 1));

        // First row
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Nombre de livraisons : "));
        row1.add(new JTextField("", 4));

        // Second row
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Durée : "));
        row2.add(new JTextField("", 4));
        row2.add(new JLabel("Retard cumulé :"));
        row2.add(new JTextField("", 4));

        // Third row
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Longueur : "));
        row3.add(new JTextField("", 4));

        topDeliveryInfoPanelContentForm.add(row1);
        topDeliveryInfoPanelContentForm.add(row2);
        topDeliveryInfoPanelContentForm.add(row3);

        // We add this "form" to topDeliveryInfoPanelContent
        topDeliveryInfoPanelContent.add(topDeliveryInfoPanelContentForm, BorderLayout.CENTER);

        return topDeliveryInfoPanelContent;
    }

    /**
     * Creates the dialog of the bottom delivery info panel.
     * @return the JPanel
     */
    private JPanel createBottomDeliveryInfoPanelContent() {
        JPanel topDeliveryInfoPanelContent = new JPanel(new BorderLayout());
        topDeliveryInfoPanelContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        topDeliveryInfoPanelContent.add(new JLabel("Livraison"), BorderLayout.PAGE_START);

        // We create here the small "form"
        JPanel bottomDeliveryInfoPanelContentForm = new JPanel(new GridLayout(5, 1));

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

        bottomDeliveryInfoPanelContentForm.add(row1);
        bottomDeliveryInfoPanelContentForm.add(row2);
        bottomDeliveryInfoPanelContentForm.add(row3);
        bottomDeliveryInfoPanelContentForm.add(row4);
        bottomDeliveryInfoPanelContentForm.add(row5);

        // We add this "form" to topDeliveryInfoPanelContent
        topDeliveryInfoPanelContent.add(bottomDeliveryInfoPanelContentForm, BorderLayout.CENTER);

        return topDeliveryInfoPanelContent;
    }
}
