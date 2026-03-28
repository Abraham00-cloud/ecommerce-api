package com.AAA.e_commerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDto(
        @NotBlank(message = "Current password is required") String currentPassword,
        @NotBlank(message = "New password is required") String newPassword,
        @NotBlank(message = "Password confirmation is required") String confirmNewPassword) {}
