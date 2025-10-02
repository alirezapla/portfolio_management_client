package co.pla.portfoliomanagement.identity.service;

import co.pla.portfoliomanagement.identity.dto.UserDto;
import co.pla.portfoliomanagement.identity.repository.UserRepository;

import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto findByUid(UUID uid) {

        return new UserDto();
    }

    public UserDto create(UserDto userDto) {
        return new UserDto();

    }

    public void delete(UUID uid) {
    }
}
