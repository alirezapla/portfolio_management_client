package co.pla.portfoliomanagement.common.dto;

import co.pla.portfoliomanagement.identity.domain.entity.User;

import java.util.UUID;

public record UserDto(UUID id, String username, String email) {
    public static UserDto fromEntity(User user) {
        return new UserDto(user.getUid(), user.getUsername(), user.getEmail());
    }
}