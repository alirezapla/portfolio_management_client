package co.pla.portfoliomanagement.gateway.security;

import co.pla.portfoliomanagement.core.exceptions.ExceptionMessages;
import co.pla.portfoliomanagement.identity.application.dto.CreateUserDto;
import co.pla.portfoliomanagement.identity.application.dto.SignupDto;
import co.pla.portfoliomanagement.identity.application.dto.UserDto;
import co.pla.portfoliomanagement.identity.application.service.UserService;
import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginDto;
import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginSuccessDto;
import co.pla.portfoliomanagement.identity.infrastructure.exceptions.InvalidPasswordException;
import co.pla.portfoliomanagement.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public AuthenticationService(JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginSuccessDto login(LoginDto loginDto) {
        User user = userService.findByUsername(loginDto.username());
        checkPassword(user, loginDto.password());
        return LoginSuccessDto.mapFromUserCredentials(user, jwtUtil.generateToken(user));
    }

    public UserDto signup(SignupDto signupDto) {
        var authorities = new HashSet<String>();
        authorities.add(UserAuthorityType.AUTHORITY_USER.getTitle());
        return userService.createUser(new CreateUserDto(signupDto, authorities));
    }

    private void checkPassword(User user, String password) {
        boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
        if (!matches) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_NOT_VALID.getTitle());
        }
    }
}

