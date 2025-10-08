package co.pla.portfoliomanagement.fixture;


import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthority;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.identity.domain.repository.UserRepository;
import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;
import co.pla.portfoliomanagement.portfolio.domain.entity.StockPosition;
import co.pla.portfoliomanagement.portfolio.domain.repository.PortfolioRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@TestConfiguration
public class TestDataFixture {

    private final PasswordEncoder passwordEncoder;

    public TestDataFixture(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Primary
    public TestData testData(UserRepository userRepository, PortfolioRepository portfolioRepository) {
        User user = new User("user", "user@mail.co", passwordEncoder.encode("pass@user"), Set.of(new UserAuthority(UserAuthorityType.AUTHORITY_USER)));
        var savedUser = userRepository.save(user);

        User admin = new User("admin", "admin@email.co", passwordEncoder.encode("pass@admin"), Set.of(new UserAuthority(UserAuthorityType.AUTHORITY_ADMIN)));
        var savedAdmin = userRepository.save(admin);

        var portfolio = new Portfolio();
        portfolio.setName("Test Portfolio");
        portfolio.setUserUid(user.getUid());
        portfolio.setBalance(10000.0);
        var stocks = new StockPosition(portfolio, "AAA", 1000, 20.0);
        portfolio.setStockPositions(Set.of(stocks));
        var savedPortfolio = portfolioRepository.save(portfolio);

        return new TestData(savedUser, savedAdmin, savedPortfolio);
    }
}

