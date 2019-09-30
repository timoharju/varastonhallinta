package varastonhallinta.ui.exceptions;

public class NoSuchRoleException extends AddUserException {
    public NoSuchRoleException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoSuchRoleException(String message) {
        super(message);
    }
}
