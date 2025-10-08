package co.pla.portfoliomanagement.portfolio.infrastructure.broker;

import co.pla.portfoliomanagement.portfolio.application.service.PredictionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQListener {

    private final PredictionService predictionService;

    public MQListener(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.receive-queue}")
    public void listen(IncomingMessage message) {
        try {
            predictionService.processPredictionResponse(message.portfolioId, message.predictionId, message.message);
        }catch (Exception e){
            log.info("error in rabbit listener => "+e.getMessage());
        }
    }
}
