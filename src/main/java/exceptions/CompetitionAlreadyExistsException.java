package exceptions;

public class CompetitionAlreadyExistsException extends RuntimeException {
    public CompetitionAlreadyExistsException(String message) {
        super(message);
    }
}
