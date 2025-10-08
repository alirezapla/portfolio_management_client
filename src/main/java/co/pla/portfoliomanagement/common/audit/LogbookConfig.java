package co.pla.portfoliomanagement.common.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import static org.zalando.logbook.core.Conditions.exclude;
import static org.zalando.logbook.core.Conditions.requestTo;

@Configuration
public class LogbookConfig {
    @Bean
    public Logbook logbook(CustomBodyFilter customBodyFilter) {
        return Logbook.builder()
                .condition(exclude(
                        requestTo("/actuator/**"),
                        requestTo("/v3/api-docs/**"),
                        requestTo("/swagger-ui/**")
                ))
                .sink(new DefaultSink(
                        new JsonHttpLogFormatter(),
                        new DefaultHttpLogWriter()
                ))
                .bodyFilter(customBodyFilter)
                .build();
    }
}
