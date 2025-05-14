package exceptions;

public class UserNameAlreadyUsedException extends Exception {
    private final String suggestedUsername;

    public UserNameAlreadyUsedException(String message, String suggestedUsername) {
        super(message);
        this.suggestedUsername = suggestedUsername;
    }

    public String getSuggestedUsername() {
        return suggestedUsername;
    }
}
