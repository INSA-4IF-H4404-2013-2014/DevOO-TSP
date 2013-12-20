package view;

import javax.swing.*;
import java.awt.*;

/**
 * @author H4404 - ABADIE Guillaume, BUISSON Nicolas, CREPET Louise, DOMINGUES Rémi, MARTIN Aline, WETTERWALD Martin
 * Date: 07/12/13
 * Time: 14:58
 * General utils for views.
 */
public class Utils {

    /**
     * Enables or disables a JTextField, but let the default background color even when disabled.
     * @param textfield The JTextfield to enable / disable
     * @param b Whether to enable or disable the JTextField
     */
    public static void enableJTextField(JTextField textfield, boolean b) {
        textfield.setEditable(b);

        if(!b) {
            textfield.setBackground(UIManager.getColor("TextField.background"));
        }
    }

    /**
     * Enables or disables a JList, but set a grey background when disabled.
     * @param list the JList to enable / disable
     * @param b Whether to enale or disable the JList
     */
    public static void enableJList(JList list, boolean b) {
        list.setEnabled(b);
        if(b) {
            list.setBackground(UIManager.getColor("List.background"));
        }
        else {
            list.setBackground(new Color(240, 240, 240));
        }
    }
}
