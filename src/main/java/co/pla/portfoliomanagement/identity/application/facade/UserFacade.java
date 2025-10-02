package co.pla.portfoliomanagement.identity.application.facade;

import co.pla.portfoliomanagement.identity.application.dto.UserDTO;
import co.pla.portfoliomanagement.identity.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public UserDTO getUserByUid(UUID uid) {
        return userService.findByUid(uid);
    }

    public UserDTO createUser(UserDTO userDto) {
        return userService.create(userDto);
    }

    public void deleteUser(UUID uid) {
        userService.delete(uid);
    }
}