package co.pla.portfoliomanagement.scheduler.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class JobNotFoundException extends BaseApplicationException {

    public JobNotFoundException(String message) {
        super(message);
    }

    public JobNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
