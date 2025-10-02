package co.pla.portfoliomanagement.identity.application.facade;

import co.pla.portfoliomanagement.identity.application.dto.SignupDto;
import co.pla.portfoliomanagement.identity.application.dto.UserDto;
import co.pla.portfoliomanagement.common.dto.LoginDto;
import co.pla.portfoliomanagement.common.dto.LoginSuccessDto;

public interface AuthFacade {

    LoginSuccessDto login(LoginDto loginDto);

    UserDto signup(SignupDto signupDto);
}
