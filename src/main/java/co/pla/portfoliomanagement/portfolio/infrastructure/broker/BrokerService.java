package co.pla.portfoliomanagement.portfolio.infrastructure.broker;

import co.pla.portfoliomanagement.portfolio.domain.repository.MessageQueueBroker;
import org.springframework.stereotype.Service;

@Service
public class BrokerService implements MessageQueueBroker {

    private final MQSender mqSender;

    public BrokerService(MQSender mqSender) {
        this.mqSender = mqSender;
    }

    @Override
    public void sendMessage(String message) {
        mqSender.send(message);
    }
}
