package exceptions;

public class ManagerLoadExсeption extends RuntimeException {
    public ManagerLoadExсeption() {
    }

    public ManagerLoadExсeption(String message) {
        super(message);
    }
}
