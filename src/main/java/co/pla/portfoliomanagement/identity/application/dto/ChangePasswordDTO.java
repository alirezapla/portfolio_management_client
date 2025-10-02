package co.pla.portfoliomanagement.identity.application.dto;

import java.util.UUID;

public record ChangePasswordDTO(UUID userUid,String currentPassword,String newPassword){}
