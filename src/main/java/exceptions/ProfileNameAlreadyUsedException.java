package exceptions;

public class ProfileNameAlreadyUsedException extends RuntimeException {
    public ProfileNameAlreadyUsedException(String message) {
        super(message);
    }
}
