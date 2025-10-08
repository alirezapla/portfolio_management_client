package co.pla.portfoliomanagement.fixture;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.portfolio.application.dto.PortfolioDto;


public record TestData(User user, User admin, PortfolioDto portfolio) {
}
