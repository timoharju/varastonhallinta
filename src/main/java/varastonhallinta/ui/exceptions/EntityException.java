package varastonhallinta.ui.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tanel
 */
public class EntityException extends Exception {

    /**
     *
     * @param message
     * @param cause
     */
    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public EntityException(String message) {
        super(message);
    }
}
