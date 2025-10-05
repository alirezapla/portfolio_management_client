package co.pla.portfoliomanagement.portfolio.infrastructure.broker;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MQListener {

    @RabbitListener(queues = "${spring.rabbitmq.receive-queue}")
    public void listen(IncomingMessage message){
        System.out.println(message);
    }
}
