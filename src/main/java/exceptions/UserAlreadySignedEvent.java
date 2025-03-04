package exceptions;

public class UserAlreadySignedEvent extends RuntimeException {
  public UserAlreadySignedEvent(String message) {
    super(message);
  }
}
