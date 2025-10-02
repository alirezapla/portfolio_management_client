package co.pla.portfoliomanagement.portfolio.application.dto;

import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;

import java.util.List;
import java.util.UUID;

public record PortfolioDto(UUID id, String name, Double balance, List<PositionDto> positions) {
    public static PortfolioDto fromEntity(Portfolio portfolio) {
        var positions = portfolio.getStockPositions().stream()
                .map(p -> new PositionDto(p.getTicker(), p.getQuantity(), p.getWeight()))
                .toList();
        return new PortfolioDto(portfolio.getUid(), portfolio.getName(), portfolio.getBalance(), positions);
    }
}