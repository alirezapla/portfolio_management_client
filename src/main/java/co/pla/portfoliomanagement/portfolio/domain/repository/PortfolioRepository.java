package co.pla.portfoliomanagement.portfolio.domain.repository;

import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PortfolioRepository {
    Optional<Portfolio> findByUid(UUID uid);
    boolean existsByName(String name);
    List<Portfolio> findByUserUid(UUID userUid);
    Portfolio save(Portfolio portfolio);

    Optional<Portfolio> findByUidWithStockPositions(@Param("uid") UUID uid);

}
