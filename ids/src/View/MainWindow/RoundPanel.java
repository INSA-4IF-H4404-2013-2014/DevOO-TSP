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
        add(createRoundPanelContent());
    }


    /**
     * Creates the dialog of the round panel.
     * @return the JPanel
     */
    private JPanel createRoundPanelContent() {
        JPanel roundPanelContent = new JPanel(new BorderLayout());
        roundPanelContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        roundPanelContent.add(new JLabel("Tournée"), BorderLayout.PAGE_START);

        // We create here the small "form"
        JPanel roundPanelContentForm = new JPanel(new GridLayout(3, 1));
        roundPanelContentForm.add(createRow1());
        roundPanelContentForm.add(createRow2());
        roundPanelContentForm.add(createRow3());

        // We add this "form" to our roundPanelContent
        roundPanelContent.add(roundPanelContentForm, BorderLayout.CENTER);

        return roundPanelContent;
    }

    private JPanel createRow1() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Nombre de livraisons : "));
        row.add(new JTextField("", 4));
        return row;
    }

    private JPanel createRow2() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Durée : "));
        row.add(new JTextField("", 4));
        row.add(new JLabel("Retard cumulé :"));
        row.add(new JTextField("", 4));
        return row;
    }

    private JPanel createRow3() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Longueur : "));
        row.add(new JTextField("", 4));
        return row;
    }
}
