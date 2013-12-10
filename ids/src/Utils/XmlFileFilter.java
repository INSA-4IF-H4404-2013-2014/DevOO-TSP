package Utils;


import javax.swing.filechooser.FileFilter;
import java.io.File;

public class XmlFileFilter extends FileFilter {

    public boolean accept(File f) {
        String extension = Utils.getExtension(f);
        if(extension != null) {
            if(extension.equals(new String("xml"))) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return "XML files only (*.xml)";
    }
}
