package com.AAA.e_commerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank(message = "User firstname is required") String firstName,
        @NotBlank(message = "User lastname is required") String lastName,
        @NotBlank(message = "User email is required") String email,
        @NotBlank(message = "User password is required") String password) {}
