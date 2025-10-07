package co.pla.portfoliomanagement.gateway.infrastructure.controller;

import co.pla.portfoliomanagement.gateway.infrastructure.util.response.SuccessfulResponseEntity;
import co.pla.portfoliomanagement.gateway.infrastructure.security.AuthenticationService;
import co.pla.portfoliomanagement.identity.application.dto.SignupDto;
import co.pla.portfoliomanagement.common.dto.LoginDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController", description = "Authentication tag")
@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(authenticationService.login(loginDto)));
    }

    @PostMapping(value = "signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupDto signupDto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(authenticationService.signup(signupDto)));
    }
}