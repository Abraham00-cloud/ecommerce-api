package com.AAA.e_commerce.payment.dto;

public record PaystackInitializeRequestDto(
        String email,
        String amount,
        String reference
) {}
