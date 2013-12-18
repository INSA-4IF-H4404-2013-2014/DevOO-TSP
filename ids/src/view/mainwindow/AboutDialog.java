package view.mainwindow;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 17/12/13
 * Time: 00:30
 * About dialog
 */
public class AboutDialog extends JDialog {

    /** The title of the about dialog */
    public final static String TITLE = "À propos";

    /** The path of the image to load */
    public final static String IMAGE_PATH = "../../img/leMagnifique.png";

    /**
     * constructor
     * @param parent the main window which contains all the information
     */
    public AboutDialog(MainWindow parent) {
        super(parent, TITLE, true);
        setResizable(false);
        setContentPane(new JLabel(new ImageIcon(getClass().getResource(IMAGE_PATH))));
        pack();
        setLocationRelativeTo(null);

        addListener();
    }

    /**
     * Adds listeners of this dialog
     */
    private void addListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                dispose();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
