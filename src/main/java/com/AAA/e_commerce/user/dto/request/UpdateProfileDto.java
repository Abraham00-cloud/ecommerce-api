package com.AAA.e_commerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileDto(
        @NotBlank(message = "User firstname is required") String firstName,
        @NotBlank(message = "User lastname is required") String lastname) {}
