package co.pla.portfoliomanagement.scheduler.application.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class JobDetailException extends BaseApplicationException {

    public JobDetailException(String message) {
        super(message);
    }

    public JobDetailException(String message, Throwable cause) {
        super(message, cause);
    }
}
