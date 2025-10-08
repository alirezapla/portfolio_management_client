package co.pla.portfoliomanagement.portfolio.infrastructure.broker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncomingMessage {
    @JsonProperty("trace_id")
    public String traceId;

    public String message;

    @JsonProperty("portfolio_id")
    public String portfolioId;

    @JsonProperty("prediction_id")
    public String predictionId;
}
