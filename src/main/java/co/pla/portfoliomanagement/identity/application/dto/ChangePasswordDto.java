package co.pla.portfoliomanagement.identity.application.dto;

import java.util.UUID;

public record ChangePasswordDto(UUID userUid,String currentPassword,String newPassword){}
