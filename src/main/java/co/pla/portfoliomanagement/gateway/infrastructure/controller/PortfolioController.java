package co.pla.portfoliomanagement.gateway.infrastructure.controller;

import co.pla.portfoliomanagement.portfolio.application.dto.PortfolioDto;
import co.pla.portfoliomanagement.portfolio.application.dto.PositionDto;
import co.pla.portfoliomanagement.portfolio.application.facade.PortfolioFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioFacade portfolioFacade;

    @PostMapping
    public ResponseEntity<PortfolioDto> create(@RequestParam String name, @RequestParam UUID userUid, @RequestParam Double balance) {
        return ResponseEntity.ok(portfolioFacade.create(name, userUid, balance));
    }

    @GetMapping("/{userUid}")
    public ResponseEntity<List<PortfolioDto>> getUserPortfolios(@PathVariable UUID userUid) {
        return ResponseEntity.ok(portfolioFacade.getUserPortfolios(userUid));
    }

    @PostMapping("/{portfolioUid}/positions")
    public ResponseEntity<PortfolioDto> addPosition(@PathVariable UUID portfolioUid, @RequestBody PositionDto dto) {
        return ResponseEntity.ok(portfolioFacade.addPosition(portfolioUid, dto));
    }
}