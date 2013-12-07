package View;

import javax.swing.*;

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
}
