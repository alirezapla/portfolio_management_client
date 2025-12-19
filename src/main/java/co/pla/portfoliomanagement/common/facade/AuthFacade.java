package co.pla.portfoliomanagement.common.facade;

import co.pla.portfoliomanagement.common.dto.SignupDto;
import co.pla.portfoliomanagement.common.dto.UserDto;
import co.pla.portfoliomanagement.common.dto.LoginDto;
import co.pla.portfoliomanagement.common.dto.LoginSuccessDto;

public interface AuthFacade {

    LoginSuccessDto login(LoginDto loginDto);

    UserDto signup(SignupDto signupDto);
}
