package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 07/12/13
 * Time: 21:15
 * This class is the small dialog window which is opened when adding a delivery.
 */
public class DeliveryDialog extends JFrame {

    public static final int PADDING = 10;

    private JComboBox client = new JComboBox();
    private JTextField newClient = new JTextField("", 10);
    private JTextField clientAddress = new JTextField("", 12);
    private JTextField timeFrameBegin = new JTextField("", 4);
    private JTextField timeFrameEnd = new JTextField("", 4);
    private JButton okButton = new JButton("Confirmer");
    private JButton cancelButton = new JButton("Annuler");

    public DeliveryDialog() {
        setContentPane(createMainPanel());
        setResizable(false);
        setAlwaysOnTop(true);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates the content panel of the window
     * @return the panel
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JLabel title = new JLabel("Livraison");
        title.setFont(new Font("Serif", Font.BOLD, 16));
        mainPanel.add(title, BorderLayout.PAGE_START);

        mainPanel.add(createForm(), BorderLayout.CENTER);

        mainPanel.add(createFooterButtons(), BorderLayout.PAGE_END);
        return mainPanel;
    }

    /**
     * Creates the panel which will directly contain the 'form'
     * @return the panel
     */
    private JPanel createForm() {
        JPanel form = new JPanel(new GridLayout(3,1));
        form.add(createRow1());
        form.add(createRow2());
        form.add(createRow3());
        return form;
    }

    private JPanel createRow1() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Client : "));
        row.add(client);

        row.add(newClient);
        return row;
    }

    private JPanel createRow2() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Adresse : "));
        row.add(clientAddress);
        return row;
    }

    private JPanel createRow3() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Plage horaire : "));
        row.add(timeFrameBegin);
        row.add(new JLabel("à"));
        row.add(timeFrameEnd);
        return row;
    }

    /**
     * Creates the panel included in the footer of this dialog.
     * They contain the action buttons.
     * @return the panel
     */
    private JPanel createFooterButtons() {
        GridLayout layout = new GridLayout(1,2);
        layout.setHgap(10);
        JPanel footer = new JPanel(layout);
        footer.add(okButton);
        footer.add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        return footer;
    }
}
