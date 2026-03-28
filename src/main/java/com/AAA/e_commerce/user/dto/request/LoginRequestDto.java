package com.AAA.e_commerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "User email is required") String email,
        @NotBlank(message = "User password is required") String password) {}
