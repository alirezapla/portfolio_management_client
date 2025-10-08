package co.pla.portfoliomanagement.portfolio.infrastructure.broker;

import co.pla.portfoliomanagement.portfolio.application.service.PredictionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MQListener {

    private final PredictionService predictionService;

    public MQListener(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.receive-queue}")
    public void listen(IncomingMessage message) {
        try {
            predictionService.processPredictionResponse(message.portfolioId, message.message);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
