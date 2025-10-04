package co.pla.portfoliomanagement.gateway.infrastructure.controller;

import co.pla.portfoliomanagement.gateway.dto.PortfolioRequest;
import co.pla.portfoliomanagement.gateway.infrastructure.util.response.SuccessfulResponseEntity;
import co.pla.portfoliomanagement.portfolio.application.dto.PositionDto;
import co.pla.portfoliomanagement.portfolio.application.facade.PortfolioFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioFacade portfolioFacade;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> create(@RequestBody PortfolioRequest portfolioRequest) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(
            portfolioFacade.create(portfolioRequest.name(), portfolioRequest.userid(), portfolioRequest.balance())));
    }

    @GetMapping("/{userUid}")
    public ResponseEntity<Object> getUserPortfolios(@PathVariable UUID userUid) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(portfolioFacade.getUserPortfolios(userUid)));
    }

    @PostMapping("/{portfolioUid}/positions")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> addPosition(@PathVariable UUID portfolioUid, @RequestBody PositionDto dto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(portfolioFacade.addPosition(portfolioUid, dto)));
    }
    @PutMapping("/{portfolioUid}/positions")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updatePosition(@PathVariable UUID portfolioUid, @RequestBody PositionDto dto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(portfolioFacade.updatePosition(portfolioUid, dto)));
    }
}