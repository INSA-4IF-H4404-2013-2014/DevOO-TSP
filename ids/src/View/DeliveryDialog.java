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
    public static final String title = "Ajout d'une livraison";
    public static final String newCli = "nouveau client";

    private DeliveryDialogController controller;
    private MainWindow parent;
    //private MainWindow parent;
    private JComboBox clientBox = new JComboBox();

    private JTextField newClient = new JTextField("", 4);
    private JLabel clientAddress = new JLabel("");
    private JTextField timeFrameBegin = new JTextField("", 4);
    private JTextField timeFrameEnd = new JTextField("", 4);
    private JButton okButton = new JButton("Confirmer");
    private JButton cancelButton = new JButton("Annuler");
    private JLabel labelNewClient = new JLabel("Nouvel id : ");


    public DeliveryDialog(DeliveryDialogController controller, MainWindow parent) {
        super(parent, title, true);
        //this.parent = parent;
        this.controller = controller;
        this.parent = parent;
        setContentPane(createMainPanel());
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        for (Client cli :parent.getRound().getClients() ){
            clientBox.addItem(cli);
        }
        clientBox.addItem(newCli);
        clientAddress.setText(parent.getMapPanel().getSelectedNode().toString());
        setNewClientEnabled(false);
        addListener();
    }

    public static int getPadding() {
        return PADDING;
    }

    public JTextField getTimeFrameEnd() {
        return timeFrameEnd;
    }

    public JTextField getTimeFrameBegin() {
        return timeFrameBegin;
    }

    public JComboBox getClientBox() {
        return clientBox;
    }

    public JTextField getNewClient() {
        return newClient;
    }

    public MainWindow getParent() {
        return parent;
    }

    /**
     *
     */
    public boolean newCliIsSelected(){
        return (clientBox.getSelectedItem().toString().compareTo(newCli) ==0);
    }

    /**
     * enable or disabled the label and text field for a new client
     * @param b
     */
    private void setNewClientEnabled( boolean b){
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

    private JPanel createRow1() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Client : "));
        row.add(clientBox);

        return row;
    }

    private JPanel createRow2() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(labelNewClient);
        row.add(newClient);
        return row;
    }

    private JPanel createRow3() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Adresse : "));
        row.add(clientAddress);
        return row;
    }

    private JPanel createRow4() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Plage horaire (format hh:mm )"));
        return row;
    }

    private JPanel createRow5() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
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

        return footer;
    }

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
                    // TODO check if repaint is relevant
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
