package View;

import Controller.DeliveryDialogController;
import Model.City.Node;
import Model.Delivery.Client;
import View.MainWindow.MainWindow;

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
public class DeliveryDialog extends JDialog {

    public static final int PADDING = 10;

    /** The dialog's title */
    public static final String title = "Ajout d'une livraison";

    /** The text for the row in the list to create a new client */
    public static final String newCli = "nouveau client";

    /** The controller of this dialog */
    private DeliveryDialogController controller;

    /** This dialog's parent window */
    private MainWindow parent;

    /** List of client (with a row for a new) */
    private JComboBox clientBox = new JComboBox();

    /** Field to set the id of a new client */
    private JTextField newClient = new JTextField("", 4);

    /** Label to indicate the address of the node clicked */
    private JLabel clientAddress = new JLabel("");

    /** Hour of the beginning of the schedule */
    private JTextField timeFrameBeginH = new JTextField("", 2);

    /** Minute of the beginning of the schedule */
    private JTextField timeFrameBeginM = new JTextField("", 2);

    /** Hours of the end of the schedule */
    private JTextField timeFrameEndH = new JTextField("", 2);

    /** Minutes of the end of the schedule */
    private JTextField timeFrameEndM = new JTextField("", 2);

    /** Confirmation button */
    private JButton okButton = new JButton("Confirmer");

    /** Cancel button */
    private JButton cancelButton = new JButton("Annuler");

    /** Label of the new client field */
    private JLabel labelNewClient = new JLabel("Nouvel id : ");

    /**
     * constructor
     * @param controller the controller of this view
     * @param parent the main window which contains all the information
     */
    public DeliveryDialog(DeliveryDialogController controller, MainWindow parent) {
        super(parent, title, true);

        this.controller = controller;
        this.parent = parent;
        setContentPane(createMainPanel());
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        for (Client cli : parent.getRound().getClients() ){
            clientBox.addItem(cli);
        }
        clientBox.addItem(newCli);
        clientAddress.setText(parent.getMapPanel().getSelectedNode().toString());
        setNewClientEnabled(false);
        addListener();
    }

    /**
     * getter
     * @return hour schedule end
     */
    public JTextField getTimeFrameEndH() {
        return timeFrameEndH;
    }

    /**
     * getter
     * @return hour schedule begin
     */
    public JTextField getTimeFrameBeginH() {
        return timeFrameBeginH;
    }

    /**
     * getter
     * @return minute schedule end
     */
    public JTextField getTimeFrameEndM() {
        return timeFrameEndM;
    }

    /**
     * getter
     * @return minute schedule begin
     */
    public JTextField getTimeFrameBeginM() {
        return timeFrameBeginM;
    }

    /**
     * getter
     * @return the list of clients
     */
    public JComboBox getClientBox() {
        return clientBox;
    }

    /**
     * getter
     * @return the new client id
     */
    public JTextField getNewClient() {
        return newClient;
    }

    /**
     * getter
     * @return the mainwindow with the information
     */
    public MainWindow getParent() {
        return parent;
    }

    /**
     * Tells whether or not the user selected newClient in the list of client
     * @return true if newClient is selected
     */
    public boolean newCliIsSelected() {
        return (clientBox.getSelectedItem().toString().compareTo(newCli) ==0);
    }

    /**
     * Enables or disables the label and text field for a new client
     * @param b
     */
    private void setNewClientEnabled( boolean b) {
        labelNewClient.setEnabled(b);
        labelNewClient.setVisible(b);
        newClient.setEnabled(b);
        newClient.setVisible(b);
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
        JPanel form = new JPanel(new GridLayout(5,1));
        form.add(createRow1());
        form.add(createRow2());
        form.add(createRow3());
        form.add(createRow4());
        form.add(createRow5());
        return form;
    }

    /**
     * Create the first row of the form
     * @return
     */
    private JPanel createRow1() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Client : "));
        row.add(clientBox);

        return row;
    }

    /**
     * Create the second row of the form
     * @return
     */
    private JPanel createRow2() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(labelNewClient);
        row.add(newClient);
        return row;
    }

    /**
     * Create the third row of the form
     * @return
     */
    private JPanel createRow3() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Adresse : "));
        row.add(clientAddress);
        return row;
    }

    /**
     * Create the forth row of the form
     * @return
     */
    private JPanel createRow4() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Plage horaire (format hh:mm )"));
        return row;
    }

    /**
     * Create the fifth row of the form
     * @return
     */
    private JPanel createRow5() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(timeFrameBeginH);
        row.add(new JLabel(":"));
        row.add(timeFrameBeginM);
        row.add(new JLabel("à"));
        row.add(timeFrameEndH);
        row.add(new JLabel(":"));
        row.add(timeFrameEndM);
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

        return footer;
    }

    /**
     * Binds all controls of this dialog to real actions.
     */
    private void addListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.cancel();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.confirm();
            }
        });

        clientBox.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newCliIsSelected()) {
                    setNewClientEnabled(true);
                    repaint();
                }
                else
                {
                    setNewClientEnabled(false);
                    repaint();
                }
            }
        });
    }
}
