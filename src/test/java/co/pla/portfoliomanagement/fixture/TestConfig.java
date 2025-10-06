package co.pla.portfoliomanagement.fixture;

import co.pla.portfoliomanagement.portfolio.infrastructure.broker.MQListener;
import co.pla.portfoliomanagement.scheduler.domain.SchedulerContract;
import org.quartz.Scheduler;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate() {
        return mock(RabbitTemplate.class);
    }

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        return mock(ConnectionFactory.class);
    }

    @Bean
    @Primary
    public MQListener mqListener() {
        return mock(MQListener.class);
    }

    @Bean
    @Primary
    public SchedulerContract schedulerContract() {
        return mock(SchedulerContract.class);
    }

    @Bean
    @Primary
    public Scheduler scheduler() {
        return mock(Scheduler.class);
    }
}