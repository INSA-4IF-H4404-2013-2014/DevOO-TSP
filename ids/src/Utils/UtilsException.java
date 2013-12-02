package Utils;

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 02/12/13
 * Time: 13:45
 * To change this template use File | Settings | File Templates.
 */
public class UtilsException extends java.lang.Exception {
    private String message;

    public UtilsException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
