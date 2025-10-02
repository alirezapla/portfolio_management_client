package co.pla.portfoliomanagement.identity.application.facade.impl;


import co.pla.portfoliomanagement.identity.application.dto.*;
import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import co.pla.portfoliomanagement.identity.application.service.UserService;
import co.pla.portfoliomanagement.identity.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    public UserDto getUserByUid(UUID uid) {
        return userService.get(uid);
    }

    @Override
    public boolean isPresent(UUID uid) {
        return userService.isPresent(uid);
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    @Override
    public User getUserByUsername(String username) {
        return userService.getByUsername(username);
    }

    @Override
    public void deleteUserByUid(UUID uid) {
        userService.delete(uid);
    }

    @Override
    public UsersDto getUsers(int page, int perPage) {
        return userService.findAll(page, perPage);
    }

    @Override
    public UserDto updateUser(EditUserDto editUserDto) {
        return userService.editUser(editUserDto);
    }

    @Override
    public String changePasswordByAdmin(ChangePasswordByAdminDto changePasswordByAdminDto) {
        return userService.changePasswordByAdmin(changePasswordByAdminDto);
    }

    @Override
    public String changePassword(ChangePasswordDto changePasswordDto) {
        return userService.changePassword(changePasswordDto);
    }


}