package Utils;

import org.w3c.dom.*;


/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

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
        String value = xmlElement.getAttribute(attribute);

        if (value == null) {
            throw new UtilsException("missing attribute '" +  attribute + "'");
        }

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
            }
            else {
                throw new UtilsException("unexpected character '" + c + "'");
            }
        }

        return value;
    }

    public static int parseUIntFromXmlAttribute(Element xmlElement, String attribute) throws UtilsException {
        String value = xmlElement.getAttribute(attribute);

        if (value == null) {
            throw new UtilsException("missing attribute '" +  attribute + "'");
        }

        return Utils.parseUInt(value);
    }

}
