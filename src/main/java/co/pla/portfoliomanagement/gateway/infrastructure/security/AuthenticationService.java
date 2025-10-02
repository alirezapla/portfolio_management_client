package co.pla.portfoliomanagement.gateway.infrastructure.security;

import co.pla.portfoliomanagement.common.exceptions.ExceptionMessages;
import co.pla.portfoliomanagement.identity.application.dto.CreateUserDto;
import co.pla.portfoliomanagement.identity.application.dto.SignupDto;
import co.pla.portfoliomanagement.identity.application.dto.UserDto;
import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.common.dto.LoginDto;
import co.pla.portfoliomanagement.common.dto.LoginSuccessDto;
import co.pla.portfoliomanagement.identity.application.exceptions.InvalidPasswordException;
import co.pla.portfoliomanagement.gateway.infrastructure.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserFacade userFacade;


    public AuthenticationService(JwtUtil jwtUtil, UserFacade userFacade, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userFacade = userFacade;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginSuccessDto login(LoginDto loginDto) {
        User user = userFacade.getUserByUsername(loginDto.username());
        checkPassword(user, loginDto.password());
        return LoginSuccessDto.mapFromUserCredentials(user, jwtUtil.generateToken(user));
    }

    public UserDto signup(SignupDto signupDto) {
        var authorities = new HashSet<String>();
        authorities.add(UserAuthorityType.AUTHORITY_USER.getTitle());
        return userFacade.createUser(new CreateUserDto(signupDto, authorities));
    }

    private void checkPassword(User user, String password) {
        boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
        if (!matches) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_NOT_VALID.getTitle());
        }
    }
}

