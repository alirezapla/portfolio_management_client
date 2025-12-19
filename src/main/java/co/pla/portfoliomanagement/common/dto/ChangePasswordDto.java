package co.pla.portfoliomanagement.common.dto;

import java.util.UUID;

public record ChangePasswordDto(UUID userUid,String currentPassword,String newPassword){}
