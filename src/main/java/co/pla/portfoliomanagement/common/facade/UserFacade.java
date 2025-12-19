package co.pla.portfoliomanagement.common.facade;

import co.pla.portfoliomanagement.common.dto.*;
import co.pla.portfoliomanagement.identity.domain.entity.User;

import java.util.UUID;

public interface UserFacade {
    UserDto getUserByUid(UUID uid);
    boolean isPresent(UUID uid);
    UserDto createUser(CreateUserDto createUserDto);
    User getUserByUsername(String username);
    void deleteUserByUid(UUID uid);
    UsersDto getUsers(int page, int perPage);
    UserDto updateUser(EditUserDto editUserDTO);
    String changePasswordByAdmin(ChangePasswordByAdminDto changePasswordByAdminDto);
    String changePassword(ChangePasswordDto changePasswordDto);
}
