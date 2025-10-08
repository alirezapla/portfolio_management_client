package co.pla.portfoliomanagement.portfolio.application.service;

import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;
import co.pla.portfoliomanagement.portfolio.domain.entity.Prediction;
import co.pla.portfoliomanagement.portfolio.domain.entity.PredictionAction;
import co.pla.portfoliomanagement.portfolio.domain.entity.StockPosition;
import co.pla.portfoliomanagement.portfolio.domain.repository.MessageQueueBroker;
import co.pla.portfoliomanagement.portfolio.domain.repository.PortfolioRepository;
import co.pla.portfoliomanagement.portfolio.domain.repository.PredictionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PredictionService {

    private final PortfolioRepository portfolioRepository;
    private final MessageQueueBroker messageQueueBroker;
    private final PredictionRepository predictionRepository;


    public PredictionService(PortfolioRepository portfolioRepository, MessageQueueBroker messageQueueBroker, PredictionRepository predictionRepository) {
        this.portfolioRepository = portfolioRepository;
        this.messageQueueBroker = messageQueueBroker;
        this.predictionRepository = predictionRepository;
    }

    public void predict() {
        var portfolios = portfolioRepository.findAll();
        for (var portfolio : portfolios) {
            Prediction prediction = createPredictionRecord(portfolio);

            var stocks = portfolioRepository.findByUidWithStockPositions(portfolio.getUid()).orElseThrow(() -> new RuntimeException(""));
            var message = stocks.getStockPositions().stream().map(StockPosition::getTicker).collect(Collectors.joining(","));
            try {
                messageQueueBroker.sendMessage(message, portfolio.getUid().toString(), prediction.getUid().toString());
            } catch (Exception e) {
                prediction.setStatus(Prediction.PredictionStatus.FAILED);
                predictionRepository.save(prediction);
                throw new RuntimeException("Failed to send prediction request", e);
            }
        }
    }

    private Prediction createPredictionRecord(Portfolio portfolio) {
        Prediction prediction = new Prediction();
        prediction.setPortfolio(portfolio);
        prediction.setStatus(Prediction.PredictionStatus.PENDING);
        return predictionRepository.save(prediction);
    }

    public void processPredictionResponse(String portfolioId,String predictionId, String predictions) {
        var prediction = predictionRepository.findByUid(UUID.fromString(predictionId)).orElseThrow(() -> new RuntimeException(""));
        for (String predicted : predictions.split(",")) {
            PredictionAction predictionAction = new PredictionAction();
            var symbol = predicted.split("_")[0];
            var action = predicted.split("_")[1];
            if (action.startsWith("+")) {
                predictionAction.setAction(PredictionAction.ActionType.BUY);
            } else if (action.startsWith("-")) {
                predictionAction.setAction(PredictionAction.ActionType.SELL);
            } else {
                predictionAction.setAction(PredictionAction.ActionType.HOLD);
            }
            var q = action.substring(1);
            predictionAction.setPrediction(prediction);
            predictionAction.setSymbol(symbol);
            predictionAction.setQuantity(Integer.valueOf(q));

            prediction.addAction(predictionAction);
        }

        prediction.setStatus(Prediction.PredictionStatus.COMPLETED);
        predictionRepository.save(prediction);
    }


}
