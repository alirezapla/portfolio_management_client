package co.pla.portfoliomanagement.common.facade;

import co.pla.portfoliomanagement.common.dto.PortfolioDto;
import co.pla.portfoliomanagement.common.dto.StockPositionDto;

import java.util.List;
import java.util.UUID;

public interface PortfolioFacade {
    List<PortfolioDto> getUserPortfolios(UUID userUid);
    PortfolioDto create(String name, UUID userUid, Double balance);
    PortfolioDto addPosition(UUID portfolioUid, StockPositionDto dto);
    PortfolioDto updatePosition(UUID portfolioUid, StockPositionDto dto);
}
