//package com.AAA.e_commerce.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException exception, HttpServletRequest request) {
//        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
//        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto(
//                LocalDateTime.now(),
//                exception.getStatusCode().value(),
//                exception.getStatusCode().toString(),
//                exception.getReason(),
//                request.getRequestURI()
//
//        );
//        return new ResponseEntity<>(apiErrorResponseDto, status);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception exception, HttpServletRequest request) {
//        if (request.getRequestURI().contains("/v3/api-docs") ||
//                request.getRequestURI().contains("/swagger")) {
//            throw new RuntimeException(exception);
//        }
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        ApiErrorResponseDto errorResponseDto = new ApiErrorResponseDto(
//                LocalDateTime.now(),
//                status.value(),
//                status.getReasonPhrase(),
//                exception.getMessage(),
//                request.getRequestURI()
//        );
//        return new ResponseEntity<>(errorResponseDto, status);
//
//    }
//
//}
