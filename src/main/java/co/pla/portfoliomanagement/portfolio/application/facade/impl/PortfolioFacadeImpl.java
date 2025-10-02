package co.pla.portfoliomanagement.portfolio.application.facade.impl;

import co.pla.portfoliomanagement.portfolio.application.dto.PortfolioDto;
import co.pla.portfoliomanagement.portfolio.application.dto.PositionDto;
import co.pla.portfoliomanagement.portfolio.application.facade.PortfolioFacade;
import co.pla.portfoliomanagement.portfolio.application.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PortfolioFacadeImpl implements PortfolioFacade {

    private final PortfolioService portfolioService;

    @Override
    public PortfolioDto create(String name, UUID userUid, Double balance) {
        return portfolioService.createPortfolio(name, userUid, balance);
    }

    @Override
    public PortfolioDto addPosition(UUID uid, PositionDto positionDto) {
        return portfolioService.addPosition(uid, positionDto);
    }

    @Override
    public PortfolioDto updatePosition(UUID portfolioUid, PositionDto positionDto) {
        return portfolioService.updatePosition(portfolioUid, positionDto);
    }


    @Override
    public List<PortfolioDto> getUserPortfolios(UUID userUid) {
        return portfolioService.getUserPortfolios(userUid);
    }
}