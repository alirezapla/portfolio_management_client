package co.pla.portfoliomanagement.identity.application.dto;

import co.pla.portfoliomanagement.identity.domain.entity.User;

import java.util.UUID;

public record UserDTO(UUID id, String username, String email, String role) {
    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getUid(), user.getUsername(), user.getEmail(), user.getRole());
    }
}