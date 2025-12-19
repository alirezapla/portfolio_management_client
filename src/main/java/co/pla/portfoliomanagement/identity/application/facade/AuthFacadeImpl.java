package co.pla.portfoliomanagement.identity.application.facade;

import co.pla.portfoliomanagement.gateway.infrastructure.security.AuthenticationService;
import co.pla.portfoliomanagement.common.dto.SignupDto;
import co.pla.portfoliomanagement.common.dto.UserDto;
import co.pla.portfoliomanagement.common.facade.AuthFacade;
import co.pla.portfoliomanagement.common.dto.LoginDto;
import co.pla.portfoliomanagement.common.dto.LoginSuccessDto;
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
