package co.pla.portfoliomanagement.portfolio.application.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class InvalidPositionUpdateException extends BaseApplicationException {

    public InvalidPositionUpdateException(String message) {
        super(message);
    }

    public InvalidPositionUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
