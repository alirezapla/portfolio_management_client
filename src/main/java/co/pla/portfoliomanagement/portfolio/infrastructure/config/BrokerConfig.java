package co.pla.portfoliomanagement.portfolio.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class BrokerConfig {
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
        return new DirectExchange("direct-exchange");
    }

    @Bean
    Binding resultBinding(Queue matchingQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(matchingQueue).to(directExchange).with("prediction");
    }
}