package varastonhallinta.ui.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tanel
 */
public class AddItemException extends Exception {

    /**
     *
     * @param message
     * @param cause
     */
    public AddItemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public AddItemException(String message) {
        super(message);
    }
}
