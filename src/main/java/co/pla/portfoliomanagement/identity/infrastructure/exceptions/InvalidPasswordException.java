package co.pla.portfoliomanagement.identity.infrastructure.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
