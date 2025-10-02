package co.pla.portfoliomanagement.identity.application.facade;

import co.pla.portfoliomanagement.identity.application.dto.*;

import java.util.UUID;

public interface UserFacade {
    UserDto getUserByUid(UUID uid);
    UserDto getUserByUserName(String username);
    void deleteUserByUid(UUID uid);
    UsersDto getUsers(int page, int perPage);
    UserDto updateUser(EditUserDto editUserDTO);
    String changePasswordByAdmin(ChangePasswordByAdminDto changePasswordByAdminDto);
    String changePassword(ChangePasswordDto changePasswordDto);
}
