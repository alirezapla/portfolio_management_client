package co.pla.portfoliomanagement.portfolio.infrastructure.jpa;

import co.pla.portfoliomanagement.portfolio.domain.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PredictionJpaRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByStatus(Prediction.PredictionStatus status);
    Optional<Prediction> findByPortfolioUidAndStatus(UUID portfolioId, Prediction.PredictionStatus status);
    Optional<Prediction> findByPortfolioUid(UUID portfolioId);
    Optional<Prediction> findByUid(UUID predictionUid);
}
