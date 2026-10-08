package com.AAA.e_commerce.payment.controller;

import com.AAA.e_commerce.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Endpoints for payment initialization")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Initialize Paystack Payment",
            description = "Generates a Paystack payment authorization URL for a specific pending order."
    )
    @PostMapping("/initialize/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> initializePayment(@PathVariable Long orderId) {
        String checkoutUrl = paymentService.initializePayment(orderId);

        Map<String, String> response = Map.of("status", "success",
                "authorization_url", checkoutUrl);
        return ResponseEntity.ok(response);
    }
}
