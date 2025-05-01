package exceptions;

public class SessionExpiredException extends RuntimeException {

    public SessionExpiredException() {
        super("La sessione è scaduta.");
    }

    public SessionExpiredException(String message) {
        super(message);
    }
}
