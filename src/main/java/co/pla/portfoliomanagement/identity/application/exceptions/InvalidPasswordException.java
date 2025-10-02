package co.pla.portfoliomanagement.identity.application.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class InvalidPasswordException extends BaseApplicationException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
