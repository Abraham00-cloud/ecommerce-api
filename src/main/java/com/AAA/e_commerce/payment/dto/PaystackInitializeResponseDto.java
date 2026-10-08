package com.AAA.e_commerce.payment.dto;

public record PaystackInitializeResponseDto(
        boolean status,
        String message,
        Data data
) {
    public record Data(
            String authorization_url,
            String access_code,
            String reference
    ) {}
}
