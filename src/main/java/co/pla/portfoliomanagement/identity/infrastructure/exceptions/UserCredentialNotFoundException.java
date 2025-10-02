package co.pla.portfoliomanagement.identity.infrastructure.exceptions;

public class UserCredentialNotFoundException extends RuntimeException {
    public UserCredentialNotFoundException(String message) {
        super(message);
    }
}
