package co.pla.portfoliomanagement.fixture;


import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthority;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.identity.domain.repository.UserRepository;
import co.pla.portfoliomanagement.portfolio.application.facade.PortfolioFacade;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;

@TestConfiguration
public class TestDataFixture {

    @Bean
    @Primary
    public TestData testData(UserRepository userRepository, PortfolioFacade portfolioFacade) {
        Set<UserAuthority> userAuthorities = new HashSet<UserAuthority>();
        userAuthorities.add(new UserAuthority(UserAuthorityType.AUTHORITY_USER));
        User user = new User("user","user@mail.co","pass@user", userAuthorities);
        var savedUser = userRepository.save(user);

        Set<UserAuthority> adminAuthorities = new HashSet<UserAuthority>();
        adminAuthorities.add(new UserAuthority(UserAuthorityType.AUTHORITY_USER));
        User admin = new User("admin","admin@email.co","pass@admin", adminAuthorities);
        var savedAdmin = userRepository.save(user);

        var portfolio = portfolioFacade.create("Test Portfolio", admin.getUid(), 10000.0);

        return new TestData(savedUser, savedAdmin, portfolio);
    }
}

