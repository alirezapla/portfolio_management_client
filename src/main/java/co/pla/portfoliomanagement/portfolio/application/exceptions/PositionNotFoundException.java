package co.pla.portfoliomanagement.portfolio.application.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class PositionNotFoundException extends BaseApplicationException {

    public PositionNotFoundException(String message) {
        super(message);
    }

    public PositionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}