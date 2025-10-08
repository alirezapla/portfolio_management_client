package co.pla.portfoliomanagement.portfolio.infrastructure.jpa;

import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;
import co.pla.portfoliomanagement.portfolio.domain.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PortfolioRepositoryImpl implements PortfolioRepository {

    private final PortfolioJpaRepository jpaRepository;

    @Override
    public List<Portfolio> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Portfolio> findByUid(UUID uid) {
        return jpaRepository.findByUid(uid);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public List<Portfolio> findByUserUid(UUID userUid) {
        return jpaRepository.findByUserUid(userUid);
    }

    @Override
    public Portfolio save(Portfolio portfolio) {
        return jpaRepository.save(portfolio);
    }

    @Override
    public Optional<Portfolio> findByUidWithStockPositions(UUID uid) {
        return jpaRepository.findByUidWithStockPositions(uid);
    }

}
