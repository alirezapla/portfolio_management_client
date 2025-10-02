package co.pla.portfoliomanagement.identity.application.service;

import co.pla.portfoliomanagement.core.exceptions.ExceptionMessages;
import co.pla.portfoliomanagement.identity.application.dto.*;
import co.pla.portfoliomanagement.identity.domain.entity.User;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthorityType;
import co.pla.portfoliomanagement.identity.domain.entity.UserAuthority;
import co.pla.portfoliomanagement.identity.domain.repository.UserRepository;

import java.util.UUID;
import java.util.stream.Collectors;

import co.pla.portfoliomanagement.identity.infrastructure.exceptions.InvalidPasswordException;
import co.pla.portfoliomanagement.identity.infrastructure.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        createUserDTO.updatePassword(passwordEncoder.encode(createUserDTO.getPassword()));
        return UserDTO.fromEntity(userRepository.save(createUserDTO.toEntity()));
    }

    public UserDTO editUser(EditUserDTO request) {

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

        return UserDTO.fromEntity(userRepository.save(user));
    }

    public UserDTO get(UUID id) {
        return UserDTO.fromEntity(
                userRepository.findByUid(id)
                        .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getTitle()))
        );
    }

    public UsersDTO findAll(int page, int perPage) {
        Page<User> all = userRepository.findAll(PageRequest.of(page - 1, perPage));
        return UsersDTO.fromEntity(all.getContent(), all.getTotalElements());
    }

    public void delete(UUID id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    public String changePasswordByAdmin(ChangePasswordByAdminDTO request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getTitle())
                );
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return "updated";
    }

    public String changePassword(ChangePasswordDTO request) {
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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.getTitle()));
    }

}