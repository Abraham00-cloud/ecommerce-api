package com.AAA.e_commerce.user.dto;

public record UpdatePasswordDto(String currentPassword, String newPassword, String confirmNewPassword) {
}
