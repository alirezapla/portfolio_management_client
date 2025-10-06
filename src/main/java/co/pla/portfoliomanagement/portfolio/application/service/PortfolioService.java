package co.pla.portfoliomanagement.portfolio.application.service;

import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import co.pla.portfoliomanagement.portfolio.application.dto.PortfolioDto;
import co.pla.portfoliomanagement.portfolio.application.dto.StockPositionDto;
import co.pla.portfoliomanagement.portfolio.application.exceptions.InvalidPositionUpdateException;
import co.pla.portfoliomanagement.portfolio.application.exceptions.PortfolioException;
import co.pla.portfoliomanagement.portfolio.application.exceptions.PortfolioNotFoundException;
import co.pla.portfoliomanagement.portfolio.application.exceptions.PositionNotFoundException;
import co.pla.portfoliomanagement.portfolio.domain.entity.Portfolio;
import co.pla.portfoliomanagement.portfolio.domain.entity.StockPosition;
import co.pla.portfoliomanagement.portfolio.domain.repository.PortfolioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserFacade userFacade;

    public PortfolioDto createPortfolio(String name, UUID userUid, Double initialBalance) {
        if (userFacade.isPresent(userUid)) {
            if (portfolioRepository.existsByName(name)) {
                throw new PortfolioException("Portfolio name already exists.");
            }
            Portfolio portfolio = new Portfolio();
            portfolio.setName(name);
            portfolio.setUserUid(userUid);
            portfolio.setBalance(initialBalance);
            return PortfolioDto.fromEntity(portfolioRepository.save(portfolio));
        }
        throw new PortfolioException("User does not exist");
    }

    public List<PortfolioDto> getUserPortfolios(UUID userUid) {
        return portfolioRepository.findByUserUid(userUid)
                .stream()
                .map(PortfolioDto::fromEntity)
                .toList();
    }

    public PortfolioDto addPosition(UUID portfolioUid, StockPositionDto StockPositionDto) {
        Portfolio portfolio = portfolioRepository.findByUid(portfolioUid)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found"));

        StockPosition pos = new StockPosition();
        pos.setPortfolio(portfolio);
        pos.setTicker(StockPositionDto.ticker());
        pos.setQuantity(StockPositionDto.quantity());
        pos.setWeight(StockPositionDto.weight());

        portfolio.getStockPositions().add(pos);

        return PortfolioDto.fromEntity(portfolioRepository.save(portfolio));
    }

    public PortfolioDto updatePosition(UUID portfolioUid, StockPositionDto StockPositionDto) {
        Portfolio portfolio = portfolioRepository.findByUidWithStockPositions(portfolioUid)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found: " + portfolioUid));

        StockPosition existingPosition = portfolio.getStockPositions().stream()
                .filter(position -> Objects.equals(position.getTicker(), StockPositionDto.ticker()))
                .findFirst()
                .orElseThrow(() -> new PositionNotFoundException(
                        "Position with symbol " + StockPositionDto.ticker() + " not found in portfolio"));
        existingPosition.setQuantity(calculateNewQuantity(existingPosition, StockPositionDto));
        existingPosition.setWeight(calculateNewWeight(existingPosition, StockPositionDto));

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioDto.fromEntity(updatedPortfolio);
    }

    private double calculateNewWeight(StockPosition existingPosition, StockPositionDto StockPositionDto) {
        var newWeight = existingPosition.getWeight() + StockPositionDto.weight();
        if (newWeight > 100) {
            throw new InvalidPositionUpdateException("Position exceeds maximum value");
        }
        return newWeight;
    }

    private int calculateNewQuantity(StockPosition existingPosition, StockPositionDto StockPositionDto) {
        var newQuantity = existingPosition.getQuantity() + StockPositionDto.quantity();
        if (newQuantity < 0) {
            throw new InvalidPositionUpdateException(
                    "Cannot reduce quantity below zero. Current: " + existingPosition.getQuantity() +
                            ", Reduction: " + StockPositionDto.quantity());
        }
        return newQuantity;
    }
}
