package com.AAA.e_commerce.exception;

import java.time.LocalDateTime;

public record ApiErrorResponseDto(
        LocalDateTime timestamp, int status, String error, String message, String path) {}
