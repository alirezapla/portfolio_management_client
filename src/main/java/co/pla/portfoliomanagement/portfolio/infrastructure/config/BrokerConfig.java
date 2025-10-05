package co.pla.portfoliomanagement.portfolio.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class BrokerConfig {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingKey}")
    private String routingKey;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Queue matchingQueue() {
        return new Queue("ai-entrypoint", true, false, false, Map.of("x-queue-type", "quorum"));
    }

    @Bean
    Queue receivingResultQueue() {
        return new Queue("prediction-result", true, false, false, Map.of("x-queue-type", "quorum"));
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding resultBinding(Queue matchingQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(matchingQueue).to(directExchange).with(routingKey);
    }
}