package Utils;

import org.w3c.dom.*;

import java.io.File;


/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static String stringFromXmlAttribute(Element xmlElement, String attribute) throws UtilsException {
        String value;

        try {
            value = xmlElement.getAttribute(attribute);
        }
        catch (Exception e) {
            throw new UtilsException("missing attribute '" +  attribute + "'");
        }

        if (value == null) {
            throw new UtilsException("missing attribute '" +  attribute + "'");
        }

        return value;
    }

    public static float parsePositiveFloat(String string) throws UtilsException {
        int stringLength = string.length();
        float value = 0.0f;
        float divisor = 1.0f;
        boolean end = false;

        for(int i = 0; i < stringLength; i++) {
            char c = string.charAt(i);

            if (!end && (c == ',' || c == '.')) {
                end = true;
            }
            else if (c >= '0' && c <= '9') {
                if (end) {
                    divisor *= 10.0f;
                }

                value = value * 10.0f + (float)(c - '0');
            }
            else {
                throw new UtilsException("unexpected character '" + c + "'");
            }
        }

        return value / divisor;
    }

    public static float parsePositiveFloatFromXmlAttribute(Element xmlElement, String attribute) throws UtilsException {
        String value = Utils.stringFromXmlAttribute(xmlElement, attribute);

        return Utils.parsePositiveFloat(value);
    }

    public static int parseUInt(String string) throws UtilsException {
        int stringLength = string.length();
        int value = 0;

        for(int i = 0; i < stringLength; i++) {
            char c = string.charAt(i);

            if (c >= '0' && c <= '9') {
                int newValue = value * 10 + (int)(c - '0');

                if (newValue < value) {
                    throw new UtilsException("overflow");
                }

                value = newValue;
            }
            else {
                throw new UtilsException("unexpected character '" + c + "'");
            }
        }

        return value;
    }

    public static int parseUIntFromXmlAttribute(Element xmlElement, String attribute) throws UtilsException {
        String value = Utils.stringFromXmlAttribute(xmlElement, attribute);

        return Utils.parseUInt(value);
    }

    /**
     * Gets file's extension
     * @param f File whose extension is to be known
     * @return The file extension
     */
    public static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1)
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

}
