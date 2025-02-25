package exceptions;

import java.io.IOException;

public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException(String message) {
        super(message);
    }
}
