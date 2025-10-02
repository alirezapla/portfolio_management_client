package co.pla.portfoliomanagement.identity.infrastructure.security;

import co.pla.portfoliomanagement.core.logging.AppLogEvent;
import co.pla.portfoliomanagement.core.exceptions.ExceptionMessages;
import co.pla.portfoliomanagement.core.logging.MyLogger;
import co.pla.portfoliomanagement.identity.application.dto.CreateUserDTO;
import co.pla.portfoliomanagement.identity.application.dto.SignupRequest;
import co.pla.portfoliomanagement.identity.application.dto.UserDTO;
import co.pla.portfoliomanagement.identity.application.service.UserService;
import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthority;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.identity.domain.repository.UserRepository;
import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginDTO;
import co.pla.portfoliomanagement.identity.infrastructure.dto.LoginSuccessDTO;
import co.pla.portfoliomanagement.identity.infrastructure.exceptions.InvalidPasswordException;
import co.pla.portfoliomanagement.identity.infrastructure.exceptions.UserCredentialNotFoundException;
import co.pla.portfoliomanagement.identity.infrastructure.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
public class AuthenticationService{
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public AuthenticationService(JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginSuccessDTO login(LoginDTO loginDTO) {
        User user = userService.findByUsername(loginDTO.getUsername());
        checkPassword(user, loginDTO.getPassword());
        return LoginSuccessDTO.mapFromUserCredentials(user, jwtUtil.generateToken(user));
    }

    public UserDTO signup(SignupRequest request) {
        var authorities = new HashSet<String>();
        authorities.add(UserAuthorityType.AUTHORITY_USER.getTitle());
       return userService.createUser(new CreateUserDTO(request, authorities));
    }

    private void checkPassword(User user, String password) {
        boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
        if (!matches) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_NOT_VALID.getTitle());
        }
    }
}

