package com.AAA.e_commerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining(", "));

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorResponseDto error =
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        status.value(),
                        "Validation Failed",
                        errorMessage,
                        request.getRequestURI());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(
            ConstraintViolationException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorResponseDto error =
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        status.value(),
                        "Constraint Violation",
                        exception.getMessage(),
                        request.getRequestURI());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(
            BadCredentialsException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiErrorResponseDto error =
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        status.value(),
                        "Unauthorized",
                        "Invalid email or password",
                        request.getRequestURI());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(
            ResponseStatusException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        ApiErrorResponseDto apiErrorResponseDto =
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        exception.getStatusCode().value(),
                        exception.getStatusCode().toString(),
                        exception.getReason(),
                        request.getRequestURI());

        return new ResponseEntity<>(apiErrorResponseDto, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiErrorResponseDto error =
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        "Access Denied: Insufficient permissions to access this resource.",
                        request.getRequestURI());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception, HttpServletRequest request) {
        if (request.getRequestURI().contains("/v3/api-docs")
                || request.getRequestURI().contains("/swagger")) {
            throw new RuntimeException(exception);
        }
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorResponseDto errorResponseDto =
                new ApiErrorResponseDto(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        exception.getMessage(),
                        request.getRequestURI());
        return new ResponseEntity<>(errorResponseDto, status);
    }
}
