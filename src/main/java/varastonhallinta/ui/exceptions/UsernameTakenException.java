package varastonhallinta.ui.exceptions;

/**
 *
 * @author tanel
 */
public class UsernameTakenException extends AddUserException {

    /**
     *
     * @param message
     * @param cause
     */
    public UsernameTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public UsernameTakenException(String message) {
        super(message);
    }
}
