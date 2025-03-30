package exceptions;

public class UserAlreadySignedCompetition extends RuntimeException {
  public UserAlreadySignedCompetition(String message) {
    super(message);
  }
}
