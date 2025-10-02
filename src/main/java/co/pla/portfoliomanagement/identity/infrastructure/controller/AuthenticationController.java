package co.pla.portfoliomanagement.identity.infrastructure.controller;

import co.pla.portfoliomanagement.core.http.response.SuccessfulRequestResponseEntity;
import co.pla.portfoliomanagement.core.logging.AppLogEvent;
import co.pla.portfoliomanagement.identity.application.dto.SignupRequest;
import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginDTO;
import co.pla.portfoliomanagement.identity.infrastructure.security.AuthenticationService;

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
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDto) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(authenticationService.login(loginDto)));
    }
    @PostMapping(value = "signup")
    public ResponseEntity<Object> login(@Valid @RequestBody SignupRequest signupDto) {
        return ResponseEntity.ok(new SuccessfulRequestResponseEntity<>(authenticationService.signup(signupDto)));
    }

}