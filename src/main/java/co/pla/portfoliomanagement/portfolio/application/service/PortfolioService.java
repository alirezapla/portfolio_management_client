package co.pla.portfoliomanagement.portfolio.application.service;

import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import co.pla.portfoliomanagement.portfolio.application.dto.PortfolioDto;
import co.pla.portfoliomanagement.portfolio.application.dto.PositionDto;
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

    public PortfolioDto addPosition(UUID portfolioUid, PositionDto positionDto) {
        Portfolio portfolio = portfolioRepository.findByUid(portfolioUid)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found"));

        StockPosition pos = new StockPosition();
        pos.setPortfolio(portfolio);
        pos.setTicker(positionDto.ticker());
        pos.setQuantity(positionDto.quantity());
        pos.setWeight(positionDto.weight());

        portfolio.getStockPositions().add(pos);

        return PortfolioDto.fromEntity(portfolioRepository.save(portfolio));
    }

    public PortfolioDto updatePosition(UUID portfolioUid, PositionDto positionDto) {
        Portfolio portfolio = portfolioRepository.findByUidWithStockPositions(portfolioUid)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found: " + portfolioUid));

        StockPosition existingPosition = portfolio.getStockPositions().stream()
                .filter(position -> Objects.equals(position.getTicker(), positionDto.ticker()))
                .findFirst()
                .orElseThrow(() -> new PositionNotFoundException(
                        "Position with symbol " + positionDto.ticker() + " not found in portfolio"));
        existingPosition.setQuantity(calculateNewQuantity(existingPosition, positionDto));
        existingPosition.setWeight(calculateNewWeight(existingPosition, positionDto));

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioDto.fromEntity(updatedPortfolio);
    }

    private double calculateNewWeight(StockPosition existingPosition, PositionDto positionDto) {
        var newWeight = existingPosition.getWeight() + positionDto.weight();
        if (newWeight > 100) {
            throw new InvalidPositionUpdateException("Position exceeds maximum value");
        }
        return newWeight;
    }

    private int calculateNewQuantity(StockPosition existingPosition, PositionDto positionDto) {
        var newQuantity = existingPosition.getQuantity() + positionDto.quantity();
        if (newQuantity < 0) {
            throw new InvalidPositionUpdateException(
                    "Cannot reduce quantity below zero. Current: " + existingPosition.getQuantity() +
                            ", Reduction: " + positionDto.quantity());
        }
        return newQuantity;
    }
}
