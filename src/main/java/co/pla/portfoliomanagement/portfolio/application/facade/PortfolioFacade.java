package co.pla.portfoliomanagement.portfolio.application.facade;

import co.pla.portfoliomanagement.portfolio.application.dto.PortfolioDto;
import co.pla.portfoliomanagement.portfolio.application.dto.PositionDto;

import java.util.List;
import java.util.UUID;

public interface PortfolioFacade {
    List<PortfolioDto> getUserPortfolios(UUID userUid);
    PortfolioDto create(String name, UUID userUid, Double balance);
    PortfolioDto addPosition(UUID portfolioUid, PositionDto dto);
    PortfolioDto updatePosition(UUID portfolioUid, PositionDto dto);
}
