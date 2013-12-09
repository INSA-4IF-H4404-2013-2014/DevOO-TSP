package View.MainWindow;

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
    private JMenuItem fileExit;
    /**
     * the import map option and shortcut
     */
    private JMenuItem openMap;
    /**
     * the import round option and shortcut
     */
    private JMenuItem openRound;
    /**
     * calculate the optimal round
     */
    private JMenuItem computeRound;
    /**
     * save the actual round
     */
    private JMenuItem saveRound;
    /**
     * add a delivery
     */
    private JMenuItem addButton;
    /**
     * delete a delivery
     */
    private JMenuItem delButton;
    /**
     * undo
     */
    private JMenuItem undoButton;
    /**
     * redo
     */
    private JMenuItem redoButton;

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

        // Exit button
        fileExit = new JMenuItem("Quitter", new ImageIcon(getClass().getResource("../../door_out.png")));
        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));


        openMap = new JMenuItem("Ouvrir une carte", new ImageIcon(getClass().getResource("../../mini_open_map.png")));
        openMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        openMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));

        openRound = new JMenuItem("Ouvrir une tournée");
        openRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        openRound.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));

        computeRound = new JMenuItem("Calculer une tournée");
        computeRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        computeRound.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

        saveRound = new JMenuItem("Sauvegarder une tournée");
        saveRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        saveRound.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        file.add(openMap);
        file.add(openRound);
        file.addSeparator();
        file.add(computeRound);
        file.add(saveRound);
        file.addSeparator();
        file.add(fileExit);

        saveRound.setEnabled(false);
        add(file);
    }

    private void createEditMenu() {
        JMenu edit = new JMenu("Éditer");
        //edit.setEnabled(false);

        addButton = new JMenuItem("Ajouter une livraison");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        addButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

        delButton = new JMenuItem("Supprimer une livraison");
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        delButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

        undoButton = new JMenuItem("annuler");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        undoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));

        redoButton = new JMenuItem("refaire");
        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
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
        view.setEnabled(false);
        add(view);
    }

    private void createHelpMenu() {
        JMenu help = new JMenu("?");
        help.setEnabled(false);
        add(help);
    }
}
