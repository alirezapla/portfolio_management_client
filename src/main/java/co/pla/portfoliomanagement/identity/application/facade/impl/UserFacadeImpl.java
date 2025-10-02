package co.pla.portfoliomanagement.identity.application.facade.impl;


import co.pla.portfoliomanagement.identity.application.dto.*;
import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import co.pla.portfoliomanagement.identity.application.service.UserService;
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
    public UserDto getUserByUserName(String username) {
        return userService.findByUsername(username);
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