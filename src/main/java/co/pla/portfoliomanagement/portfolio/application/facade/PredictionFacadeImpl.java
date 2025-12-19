package co.pla.portfoliomanagement.portfolio.application.facade;

import co.pla.portfoliomanagement.common.facade.PredictionFacade;
import co.pla.portfoliomanagement.portfolio.application.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PredictionFacadeImpl implements PredictionFacade {

    private final PredictionService predictionService;

    @Override
    public void predict() {
        predictionService.predict();
    }
}
