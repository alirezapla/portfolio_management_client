package co.pla.portfoliomanagement.identity.application.dto;


import java.util.Set;
import java.util.UUID;


public record EditUserDTO(UUID uid, String username, Set<String> authorities) {
}
