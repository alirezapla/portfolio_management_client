package co.pla.portfoliomanagement.scheduler.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class JobConfigurationException extends BaseApplicationException {

    public JobConfigurationException(String message) {
        super(message);
    }

    public JobConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
