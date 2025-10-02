package co.pla.portfoliomanagement.portfolio.application.exceptions;

import co.pla.portfoliomanagement.common.exceptions.BaseApplicationException;

public class PortfolioNotFoundException extends BaseApplicationException {

    public PortfolioNotFoundException(String message) {
        super(message);
    }

    public PortfolioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
