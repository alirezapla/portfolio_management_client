package co.pla.portfoliomanagement.common.dto;


import java.util.Set;
import java.util.UUID;


public record EditUserDto(UUID uid, String username, Set<String> authorities) {
}
