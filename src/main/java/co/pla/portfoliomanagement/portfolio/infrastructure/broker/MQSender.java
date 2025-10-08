package co.pla.portfoliomanagement.portfolio.infrastructure.broker;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

@Service
public class MQSender {

    private final AmqpTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.send-queue}")
    private String queueName;

    public MQSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String message, String portfolioId) {
        var rabbitMessage = messageBuilder(UUID.randomUUID().toString(), portfolioId, message);
        rabbitTemplate.convertAndSend(queueName, rabbitMessage);
    }

    private HashMap messageBuilder(String traceId, String portfolioId, String message) {
        var messageMap = new HashMap<String, String>();
        messageMap.put("portfolio_id", portfolioId);
        messageMap.put("trace_id", traceId);
        messageMap.put("message", message);
        return messageMap;
    }
}