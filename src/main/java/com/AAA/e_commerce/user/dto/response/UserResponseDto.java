package com.AAA.e_commerce.user.dto.response;

public record UserResponseDto(
        Long userId, String firstName, String lastName, String email, Long cartId) {}
