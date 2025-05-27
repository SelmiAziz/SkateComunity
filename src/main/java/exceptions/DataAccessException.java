package exceptions;

public class DataAccessException extends Exception {
    private static final String DEFAULT_MESSAGE = "An error occurred during data access operation.";

    public DataAccessException() {
        super(DEFAULT_MESSAGE);
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}