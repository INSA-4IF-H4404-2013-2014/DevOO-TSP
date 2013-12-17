package View.MainWindow;

import javax.swing.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 02/01/13
 * Time: 22:20
 * This class is the JToolBar at the top of the main window
 */
public class TopToolBar extends JToolBar {
    private JButton loadMap = new JButton("Charger carte");
    private JButton loadRound = new JButton("Charger tournée");
    private JButton saveRound = new JButton("Enregistrer tournée");
    private JButton add = new JButton("Ajouter");
    private JButton delete = new JButton("Supprimer");
    private JButton undo = new JButton("Défaire");
    private JButton redo = new JButton("Refaire");

    public TopToolBar() {
        setFloatable(false);
        //setRollover(true);

        add(loadMap);

        addSeparator();

        add(loadRound);
        add(saveRound);

        addSeparator();

        add(add);
        add(delete);

        addSeparator();

        add(undo);
        add(redo);


        loadMap.setEnabled(false);
        loadRound.setEnabled(false);
        saveRound.setEnabled(false);
        add.setEnabled(false);
        delete.setEnabled(false);
        undo.setEnabled(false);
        redo.setEnabled(false);
    }
}
