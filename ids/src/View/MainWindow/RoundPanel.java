package View.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 07/12/13
 * Time: 14:07
 * In the main window, this is the right top part
 */
public class RoundPanel extends JPanel {

    JTextField deliveryCount;
    JTextField duration;
    JTextField delay;
    JTextField distance;

    public RoundPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.GRAY);
        add(createTopDeliveryInfoPanelContent());
    }


    /**
     * Creates the dialog of the round panel.
     * @return the JPanel
     */
    private JPanel createTopDeliveryInfoPanelContent() {
        JPanel roundPanelContent = new JPanel(new BorderLayout());
        roundPanelContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        roundPanelContent.add(new JLabel("Tournée"), BorderLayout.PAGE_START);

        // We create here the small "form"
        JPanel roundPanelContentForm = new JPanel(new GridLayout(3, 1));

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

        roundPanelContentForm.add(row1);
        roundPanelContentForm.add(row2);
        roundPanelContentForm.add(row3);

        // We add this "form" to topDeliveryInfoPanelContent
        roundPanelContent.add(roundPanelContentForm, BorderLayout.CENTER);

        return roundPanelContent;
    }
}
