package co.pla.portfoliomanagement.identity.application.facade.impl;

import co.pla.portfoliomanagement.gateway.security.AuthenticationService;
import co.pla.portfoliomanagement.identity.application.dto.SignupDto;
import co.pla.portfoliomanagement.identity.application.dto.UserDto;
import co.pla.portfoliomanagement.identity.application.facade.AuthFacade;
import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginDto;
import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {
    private final AuthenticationService authenticationService;


    @Override
    public LoginSuccessDto login(LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }

    @Override
    public UserDto signup(SignupDto signupDto) {
        return authenticationService.signup(signupDto);
    }
}
