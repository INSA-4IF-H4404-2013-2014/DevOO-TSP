package View.MainWindow;

import Controller.MainWindowController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/01/13
 * Time: 15:30
 * This class is the JMenuBar at the top of the main window
 */
public class TopMenuBar extends JMenuBar{
    /**
     * the exit option and shortcut
      */
    protected JMenuItem fileExit;
    /**
     * the import map option and shortcut
     */
    protected JMenuItem openMap;
    /**
     * the import round option and shortcut
     */
    protected JMenuItem openRound;
    /**
     * calculate the optimal round
     */
    protected JMenuItem computeRound;
    /**
     * save the actual round
     */
    protected JMenuItem saveRound;
    /**
     * add a delivery
     */
    protected JMenuItem addButton;
    /**
     * delete a delivery
     */
    protected JMenuItem delButton;
    /**
     * undo
     */
    protected JMenuItem undoButton;
    /**
     * redo
     */
    protected JMenuItem redoButton;

    public JMenuItem getOpenMap() {
        return openMap;
    }

    public JMenuItem getFileExit() {
        return fileExit;
    }

    public JMenuItem getOpenRound() {
        return openRound;
    }

    public JMenuItem getComputeRound() {
        return computeRound;
    }

    public JMenuItem getSaveRound() {
        return saveRound;
    }

    public JMenuItem getAddButton() {
        return addButton;
    }

    public JMenuItem getDelButton() {
        return delButton;
    }

    public JMenuItem getUndoButton() {
        return undoButton;
    }

    public JMenuItem getRedoButton() {
        return redoButton;
    }

    public TopMenuBar() {
        createFileMenu();
        createEditMenu();
        createViewMenu();
        createHelpMenu();
    }

    private void createFileMenu() {
        JMenu file = new JMenu("Fichier");
        file.setMnemonic(KeyEvent.VK_F);

        fileExit = new JMenuItem("Quitter", new ImageIcon(getClass().getResource("./../../img/door_out.png")));
        fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

        openMap = new JMenuItem("Ouvrir une carte", new ImageIcon(getClass().getResource("../../img/mini_open_map.png")));

        openRound = new JMenuItem("Ouvrir une tournée", new ImageIcon(getClass().getResource("../../img/mini_open_round.png")));

        computeRound = new JMenuItem("Calculer une tournée");

        saveRound = new JMenuItem("Sauvegarder une tournée", new ImageIcon(getClass().getResource("../../img/mini_save_round.png")));
        saveRound.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        file.add(openMap);
        file.add(openRound);
        file.addSeparator();
        file.add(computeRound);
        file.add(saveRound);
        file.addSeparator();
        file.add(fileExit);

        openRound.setEnabled(false);
        computeRound.setEnabled(false);
        saveRound.setEnabled(false);

        add(file);
    }

    private void createEditMenu() {
        JMenu edit = new JMenu("Éditer");
        edit.setMnemonic(KeyEvent.VK_E);

        addButton = new JMenuItem("Ajouter une livraison", new ImageIcon(getClass().getResource("../../img/mini_add.png")));

        delButton = new JMenuItem("Supprimer une livraison", new ImageIcon(getClass().getResource("../../img/mini_delete.png")));

        undoButton = new JMenuItem("Annuler", new ImageIcon(getClass().getResource("../../img/mini_undo.png")));
        undoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));

        redoButton = new JMenuItem("Refaire", new ImageIcon(getClass().getResource("../../img/mini_redo.png")));
        redoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));

        edit.add(addButton);
        edit.add(delButton);
        edit.addSeparator();
        edit.add(undoButton);
        edit.add(redoButton);

        addButton.setEnabled(false);
        delButton.setEnabled(false);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        add(edit);
    }

    private void createViewMenu() {
        JMenu view = new JMenu("Affichage");
        view.setMnemonic(KeyEvent.VK_A);
        view.setEnabled(false);
        add(view);
    }

    private void createHelpMenu() {
        JMenu help = new JMenu("?");
        help.setEnabled(false);
        add(help);
    }

    public void addListener(final MainWindowController controller) {
        openMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.loadNetwork();
            }
        });
        openRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.loadRound();
            }
        });

        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exit();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addDelivery();
            }
        });

        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeDelivery();
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.historyUndo();
            }
        });

        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.historyRedo();
            }
        });
    }
}
