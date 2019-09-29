package varastonhallinta.ui.exceptions;


public class UsernameTakenException extends AddUserException {
    public UsernameTakenException(String message, Throwable cause) {
        super(message, cause);
    }
    public UsernameTakenException(String message) {
        super(message);
    }
}
