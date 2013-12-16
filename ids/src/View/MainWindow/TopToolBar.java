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
    /** The 'open map' button */
    protected JButton loadMap = new JButton();

    /** The 'open round' button */
    protected JButton loadRound = new JButton();

    /** The 'save round' button */
    protected JButton saveRound = new JButton();

    /** The 'add delivery' button */
    protected JButton add = new JButton();

    /** The 'remove delivery' button */
    protected JButton delete = new JButton();

    /** The 'undo' button */
    protected JButton undo = new JButton();

    /** The 'redo' button */
    protected JButton redo = new JButton();

    /**
     * Gets the loadMap button
     * @return the loadMap button
     */
    public JButton getLoadMap() {
        return loadMap;
    }

    /**
     * Gets the loadRound button
     * @return the loadRound button
     */
    public JButton getLoadRound() {
        return loadRound;
    }

    /**
     * Gets the saveRound button
     * @return the saveround button
     */
    public JButton getSaveRound() {
        return saveRound;
    }

    /**
     * Gets the add button
     * @return the add button
     */
    public JButton getAdd() {
        return add;
    }

    /**
     * Gets the delete button
     * @return the delete button
     */
    public JButton getDelete() {
        return delete;
    }

    /**
     * Gets the undo button
     * @return the undo button
     */
    public JButton getUndo() {
        return undo;
    }

    /**
     * Gets the redo button
     * @return the redo button
     */
    public JButton getRedo() {
        return redo;
    }

    /**
     * Constructor
     */
    public TopToolBar() {
        setFloatable(false);
        //setRollover(true);

        loadMap.setIcon(new ImageIcon("./src/img/open_map.png"));
        add(loadMap);
        loadRound.setIcon(new ImageIcon("./src/img/open_round.png"));
        add(loadRound);

        addSeparator();
        saveRound.setIcon(new ImageIcon("./src/img/save_round.png"));
        add(saveRound);

        addSeparator();
        add.setIcon(new ImageIcon("./src/img/add.png"));
        add(add);
        delete.setIcon(new ImageIcon("./src/img/delete.png"));
        add(delete);

        addSeparator();
        undo.setIcon(new ImageIcon("./src/img/undo.png"));
        add(undo);
        redo.setIcon(new ImageIcon("./src/img/redo.png"));
        add(redo);

        loadRound.setEnabled(false);
        saveRound.setEnabled(false);
        add.setEnabled(false);
        delete.setEnabled(false);
        undo.setEnabled(false);
        redo.setEnabled(false);
    }

    /**
     * Requests this toolbar to bind all of its controls to real actions.
     * @param controller A reference to our main controller
     */
    public void addListener(final MainWindowController controller) {
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addDelivery();
            }
        });
        loadMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.loadNetwork();
            }
        });
        loadRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.loadRound();
            }
        });
        saveRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exportRound();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeDelivery();
            }
        });

        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.historyUndo();
            }
        });

        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.historyRedo();
            }
        });
    }
}
