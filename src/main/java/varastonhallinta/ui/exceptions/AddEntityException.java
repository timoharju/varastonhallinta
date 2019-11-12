package varastonhallinta.ui.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tanel
 */
public class AddEntityException extends Exception {

    /**
     *
     * @param message
     * @param cause
     */
    public AddEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public AddEntityException(String message) {
        super(message);
    }
}
