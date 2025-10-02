package co.pla.portfoliomanagement.portfolio.application.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class PortfolioException extends BaseApplicationException {

    public PortfolioException(String message) {
        super(message);
    }

    public PortfolioException(String message, Throwable cause) {
        super(message, cause);
    }
}
