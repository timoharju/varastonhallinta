package varastonhallinta.ui.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tanel
 */
public class AddUserException extends Exception {

    /**
     *
     * @param message
     * @param cause
     */
    public AddUserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public AddUserException(String message) {
        super(message);
    }
}
