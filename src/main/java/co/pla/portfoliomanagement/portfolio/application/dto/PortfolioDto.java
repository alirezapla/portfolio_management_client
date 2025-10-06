package co.pla.portfoliomanagement.portfolio.application.dto;

import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;

import java.util.List;
import java.util.UUID;

public record PortfolioDto(UUID id, UUID userId, String name, Double balance, List<StockPositionDto> stockPositions) {
    public static PortfolioDto fromEntity(Portfolio portfolio) {
        var positions = portfolio.getStockPositions().stream()
                .map(p -> new StockPositionDto(p.getTicker(), p.getQuantity(), p.getWeight()))
                .toList();
        return new PortfolioDto(portfolio.getUid(), portfolio.getUserUid(), portfolio.getName(), portfolio.getBalance(), positions);
    }
}