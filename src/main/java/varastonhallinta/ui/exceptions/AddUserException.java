package varastonhallinta.ui.exceptions;

import java.util.ArrayList;
import java.util.List;

public class AddUserException extends Exception {
    public AddUserException(String message, Throwable cause) {
        super(message, cause);
    }
    public AddUserException(String message) {
        super(message);
    }
}
