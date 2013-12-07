package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public class DeliveryWindow extends JFrame {

    public static final int PADDING = 10;

    private JComboBox client = new JComboBox();
    private JTextField clientAddress = new JTextField("", 12);
    private JTextField timeFrameBegin = new JTextField("", 4);
    private JTextField timeFrameEnd = new JTextField("", 4);
    private JButton okButton = new JButton("Confirmer");
    private JButton cancelButton = new JButton("Annuler");

    public DeliveryWindow() {
        setContentPane(createMainPanel());
        setResizable(false);
        setAlwaysOnTop(true);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

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
