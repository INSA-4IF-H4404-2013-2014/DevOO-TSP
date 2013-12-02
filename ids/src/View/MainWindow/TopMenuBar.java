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
    public TopMenuBar() {
        createFileMenu();
        createEditMenu();
        createViewMenu();
        createHelpMenu();
    }

    private void createFileMenu() {
        JMenu file = new JMenu("Fichier");

        // Exit button
        JMenuItem fileExit = new JMenuItem("Quitter", new ImageIcon(getClass().getResource("../../door_out.png")));
        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

        file.add(fileExit);

        add(file);
    }

    private void createEditMenu() {
        JMenu edit = new JMenu("Éditer");
        edit.setEnabled(false);
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
