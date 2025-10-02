package co.pla.portfoliomanagement.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.HashMap;

@Component
public class MyLogger {
    private final Logger logger;

    public MyLogger() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void doLog(LogLevel logLevel, AppLogEvent event, Object payload) {
        String message = new HashMap<String, Object>() {{
            put("Event", event);
            put("Body", payload);
            put("timestamp", ZonedDateTime.now().toInstant().toEpochMilli());
        }}.toString();
        switch (logLevel) {
            case DEBUG:
                logger.debug(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            case INFO:
                logger.info(message);
            case OFF:
        }

    }
}
