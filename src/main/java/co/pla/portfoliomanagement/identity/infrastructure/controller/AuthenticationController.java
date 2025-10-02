package co.pla.portfoliomanagement.identity.infrastructure.controller;

import co.pla.portfoliomanagement.core.http.response.SuccessfulRequestResponseEntity;
import co.pla.portfoliomanagement.identity.application.dto.SignupDto;
import co.pla.portfoliomanagement.gateway.security.AuthenticationService;

import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(authenticationService.login(loginDto)));
    }

    @PostMapping(value = "signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupDto signupDto) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(authenticationService.signup(signupDto)));
    }

}