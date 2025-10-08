package co.pla.portfoliomanagement.portfolio.domain.repository;

import co.pla.portfoliomanagement.portfolio.domain.entity.Prediction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PredictionRepository {

    Prediction save(Prediction prediction);

    Optional<Prediction> findByPortfolioIdAndStatus(UUID portfolioId, Prediction.PredictionStatus status);
    Optional<Prediction> findByPortfolioId(UUID portfolioId);
    Optional<Prediction> findByUid(UUID predictionId);

    List<Prediction> findPendingItems();

    List<Prediction> findCompletedItems();

    List<Prediction> findFailedItems();
}
