package varastonhallinta.ui.exceptions;

/**
 *
 * @author tanel
 */
public class ItemnameTakenException extends AddItemException {

    /**
     *
     * @param message
     * @param cause
     */
    public ItemnameTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public ItemnameTakenException(String message) {
        super(message);
    }
}
