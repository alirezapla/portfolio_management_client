package co.pla.portfoliomanagement.identity.facade;

import co.pla.portfoliomanagement.identity.dto.UserDto;
import co.pla.portfoliomanagement.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public UserDto getUserByUid(UUID uid) {
        return userService.findByUid(uid);
    }

    public UserDto createUser(UserDto userDto) {
        return userService.create(userDto);
    }

    public void deleteUser(UUID uid) {
        userService.delete(uid);
    }
}