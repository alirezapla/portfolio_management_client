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

    public void send(String message) {
        var rabbitMessage = messageBuilder("123456",message);
        rabbitTemplate.convertAndSend(queueName, rabbitMessage);
        System.out.println("Send msg to consumer= " + rabbitMessage.entrySet()
                .stream()
                .map(Object::toString)
                .collect(joining(",")) +" ");
    }

    private HashMap messageBuilder(String traceId,String message) {
        var messageMap = new HashMap<String, String>();
        messageMap.put("portfolio_id", UUID.randomUUID().toString());
        messageMap.put("trace_id", traceId);
        messageMap.put("message", message);
        return messageMap;
    }
}