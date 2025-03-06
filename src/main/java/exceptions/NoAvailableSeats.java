package exceptions;

public class NoAvailableSeats extends RuntimeException {
    public NoAvailableSeats(String message) {
        super(message);
    }
}
