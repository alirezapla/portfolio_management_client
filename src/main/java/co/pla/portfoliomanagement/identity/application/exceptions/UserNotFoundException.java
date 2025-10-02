package co.pla.portfoliomanagement.identity.application.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class UserNotFoundException extends BaseApplicationException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
