package co.pla.portfoliomanagement.portfolio.infrastructure.jpa;

import co.pla.portfoliomanagement.portfolio.domain.entity.Prediction;
import co.pla.portfoliomanagement.portfolio.domain.repository.PredictionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PredictionRepositoryImpl implements PredictionRepository {
    private final PredictionJpaRepository predictionJpaRepository;

    public PredictionRepositoryImpl(PredictionJpaRepository predictionJpaRepository) {
        this.predictionJpaRepository = predictionJpaRepository;
    }

    @Override
    public Prediction save(Prediction prediction) {
        return predictionJpaRepository.save(prediction);
    }

    @Override
    public Optional<Prediction> findByPortfolioIdAndStatus(UUID portfolioId, Prediction.PredictionStatus status) {
        return predictionJpaRepository.findByPortfolioUidAndStatus(portfolioId, status);
    }

    @Override
    public Optional<Prediction> findByPortfolioId(UUID portfolioId) {
        return predictionJpaRepository.findByPortfolioUid(portfolioId);
    }

    @Override
    public Optional<Prediction> findByUid(UUID predictionUid) {
        return predictionJpaRepository.findByUid(predictionUid);
    }


    @Override
    public List<Prediction> findPendingItems() {
        return predictionJpaRepository.findByStatus(Prediction.PredictionStatus.PENDING);
    }

    @Override
    public List<Prediction> findCompletedItems() {
        return predictionJpaRepository.findByStatus(Prediction.PredictionStatus.COMPLETED);
    }

    @Override
    public List<Prediction> findFailedItems() {
        return predictionJpaRepository.findByStatus(Prediction.PredictionStatus.FAILED);
    }
}
