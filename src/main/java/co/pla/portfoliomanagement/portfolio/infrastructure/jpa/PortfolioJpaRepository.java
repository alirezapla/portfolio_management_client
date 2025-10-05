package co.pla.portfoliomanagement.portfolio.infrastructure.jpa;

import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PortfolioJpaRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUid(UUID uid);
    List<Portfolio> findByUserUid(UUID userUid);
    boolean existsByName(String name);

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.stockPositions WHERE p.uid = :uid")
    Optional<Portfolio> findByUidWithStockPositions(@Param("uid") UUID uid);
}