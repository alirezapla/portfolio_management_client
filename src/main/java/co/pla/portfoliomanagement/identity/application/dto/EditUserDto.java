package co.pla.portfoliomanagement.identity.application.dto;


import java.util.Set;
import java.util.UUID;


public record EditUserDto(UUID uid, String username, Set<String> authorities) {
}
