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
    protected JButton saveRound = new JButton();
    protected JButton add = new JButton();
    protected JButton delete = new JButton();
    protected JButton undo = new JButton();
    protected JButton redo = new JButton();

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
