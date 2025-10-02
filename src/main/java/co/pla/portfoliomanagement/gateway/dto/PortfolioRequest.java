package co.pla.portfoliomanagement.gateway.dto;

import java.util.UUID;

public record PortfolioRequest(String name, UUID userid, Double balance) {
}
