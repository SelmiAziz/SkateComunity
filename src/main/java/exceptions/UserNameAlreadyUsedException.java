package exceptions;

public class UserNameAlreadyUsedException extends RuntimeException {
    public UserNameAlreadyUsedException(String message) {
        super(message);
    }
}
