package View.MainWindow;

import javax.swing.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/01/13
 * Time: 15:30
 * This class is the JMenuBar at the top of the main window
 */
public class TopMenuBar extends JMenuBar{
    private JMenu file = new JMenu("Fichier");
    private JMenu edit = new JMenu("Éditer");
    private JMenu view = new JMenu("Affichage");
    private JMenu help = new JMenu("?");

    public TopMenuBar()
    {
        add(file);
        add(edit);
        add(view);
        add(help);
    }
}
