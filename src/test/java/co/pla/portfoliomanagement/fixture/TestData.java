package co.pla.portfoliomanagement.fixture;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;


public record TestData(User user, User admin, Portfolio portfolio) {
}
