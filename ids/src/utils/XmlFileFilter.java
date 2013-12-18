package utils;


import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 10/12/13
 * Time: 20:45
 * This is a filter for JFileChooser allowing only XML files and folders.
 */
public class XmlFileFilter extends FileFilter {

    public boolean accept(File f) {

        // We want user to be able to see directories
        if(f.isDirectory()) {
            return true;
        }

        // We want user to be able to see XML files
        String extension = Utils.getExtension(f);
        if(extension != null) {
            if(extension.equals(new String("xml"))) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return "Fichiers XML seulement (*.xml)";
    }
}
