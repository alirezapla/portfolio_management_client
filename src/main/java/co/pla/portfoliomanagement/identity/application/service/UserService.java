package co.pla.portfoliomanagement.identity.application.service;

import co.pla.portfoliomanagement.common.exceptions.ExceptionMessages;
import co.pla.portfoliomanagement.identity.application.dto.*;
import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthority;
import co.pla.portfoliomanagement.identity.domain.repository.UserRepository;

import java.util.UUID;
import java.util.stream.Collectors;

import co.pla.portfoliomanagement.identity.application.exceptions.InvalidPasswordException;
import co.pla.portfoliomanagement.identity.application.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(CreateUserDto createUserDto) {
        createUserDto.updatePassword(passwordEncoder.encode(createUserDto.getPassword()));
        return UserDto.fromEntity(userRepository.save(createUserDto.toEntity()));
    }

    public boolean isPresent(UUID uid){
        return userRepository.isPresent(uid);
    }

    public UserDto editUser(EditUserDto request) {

        User user = userRepository.findByUid(request.uid())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getTitle())
                );

        user.updateUser(
                request.username(),
                request.authorities()
                        .stream()
                        .map(UserAuthorityType::getByUserAuthorityTitle)
                        .map(UserAuthority::new)
                        .collect(Collectors.toSet()));

        return UserDto.fromEntity(userRepository.save(user));
    }

    public UserDto get(UUID id) {
        return UserDto.fromEntity(
                userRepository.findByUid(id)
                        .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getTitle()))
        );
    }

    public UsersDto findAll(int page, int perPage) {
        Page<User> all = userRepository.findAll(PageRequest.of(page - 1, perPage));
        return UsersDto.fromEntity(all.getContent(), all.getTotalElements());
    }

    public void delete(UUID id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    public String changePasswordByAdmin(ChangePasswordByAdminDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getTitle())
                );
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return "Password changed Successfully";
    }

    public String changePassword(ChangePasswordDto request) {
        User user = userRepository.findByUid(request.userUid())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.RECORD_NOT_FOUND.getTitle())
                );

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash()))
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_NOT_VALID.getTitle());

        if (request.newPassword().equals(request.currentPassword()))
            throw new InvalidPasswordException(ExceptionMessages.CURRENT_PASSWORD_IS_EQUAL_TO_NEW_PASSWORD.getTitle());

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return "Password changed Successfully";
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getTitle())
        );
    }

}