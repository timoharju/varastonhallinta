package varastonhallinta.ui.exceptions;

/**
 *
 * @author tanel
 */
public class NoSuchRoleException extends AddUserException {

    /**
     *
     * @param message
     * @param cause
     */
    public NoSuchRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public NoSuchRoleException(String message) {
        super(message);
    }
}
