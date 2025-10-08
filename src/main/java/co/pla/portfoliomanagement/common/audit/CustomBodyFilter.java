package co.pla.portfoliomanagement.common.audit;

import org.springframework.stereotype.Component;
import org.zalando.logbook.BodyFilter;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

@Component
public class CustomBodyFilter implements BodyFilter {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("(\"password\"\\s*:\\s*\")[^\"]*(\")");
    private static final Pattern JWT_PATTERN = Pattern.compile("(\"jwt\"\\s*:\\s*\")[^\"]*(\")");
    private static final Pattern TOKEN_PATTERN = Pattern.compile("(\"token\"\\s*:\\s*\")[^\"]*(\")");

    @Override
    public String filter(@Nullable String contentType, String body) {
        if (body == null || !contentType.contains("application/json")) {
            return body;
        }

        body = PASSWORD_PATTERN.matcher(body).replaceAll("***");
        body = JWT_PATTERN.matcher(body).replaceAll("****");
        body = TOKEN_PATTERN.matcher(body).replaceAll("***");

        return body;
    }
}
