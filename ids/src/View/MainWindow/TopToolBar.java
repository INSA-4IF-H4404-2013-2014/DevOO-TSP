package View.MainWindow;

import Controller.MainWindowController;
import View.DeliveryDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/01/13
 * Time: 22:20
 * This class is the JToolBar at the top of the main window
 */
public class TopToolBar extends JToolBar {
    protected JButton loadMap = new JButton();
    protected JButton loadRound = new JButton();
    protected JButton saveRound = new JButton("Enregistrer tournée");
    protected JButton add = new JButton("Ajouter");
    protected JButton delete = new JButton("Supprimer");
    protected JButton undo = new JButton("Défaire");
    protected JButton redo = new JButton("Refaire");

    public JButton getLoadMap() {
        return loadMap;
    }

    public JButton getLoadRound() {
        return loadRound;
    }

    public JButton getSaveRound() {
        return saveRound;
    }

    public JButton getAdd() {
        return add;
    }

    public JButton getDelete() {
        return delete;
    }

    public JButton getUndo() {
        return undo;
    }

    public JButton getRedo() {
        return redo;
    }

    public TopToolBar() {
        setFloatable(false);
        //setRollover(true);

        loadMap.setIcon(new ImageIcon("./src/open_map.png"));
        add(loadMap);

        addSeparator();

        loadRound.setIcon(new ImageIcon("./src/open_round.png"));
        add(loadRound);
        add(saveRound);

        addSeparator();

        add(add);
        add(delete);

        addSeparator();

        add(undo);
        add(redo);

        saveRound.setEnabled(false);
        add.setEnabled(false);
        delete.setEnabled(false);
        undo.setEnabled(false);
        redo.setEnabled(false);
    }

    public void addListener(final MainWindowController controller) {
        // Add button
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addDelivery();
            }
        });

        // Load map button
        loadMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.loadNetwork();
            }
        });
    }
}
