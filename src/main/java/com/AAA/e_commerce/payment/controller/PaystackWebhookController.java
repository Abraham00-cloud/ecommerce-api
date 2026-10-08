package com.AAA.e_commerce.payment.controller;

import com.AAA.e_commerce.payment.service.PaystackPaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class PaystackWebhookController {
    private final PaystackPaymentServiceImpl paystackPaymentService;

    @PostMapping("/paystack")
    public ResponseEntity<Void> handlePaystackWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "x-paystack-signature", required = false) String signature
    ) {
        paystackPaymentService.processWebhook(payload, signature);

        return ResponseEntity.ok().build();
    }
}
