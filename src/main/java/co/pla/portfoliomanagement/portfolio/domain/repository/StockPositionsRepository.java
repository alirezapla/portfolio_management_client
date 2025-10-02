package co.pla.portfoliomanagement.portfolio.domain.repository;

import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;
import co.pla.portfoliomanagement.portfolio.domain.entity.StockPosition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockPositionsRepository {
    Optional<StockPosition> findByUid(UUID uid);
    List<StockPosition> findByUserUid(UUID userUid);
    Portfolio save(Portfolio portfolio);
}
