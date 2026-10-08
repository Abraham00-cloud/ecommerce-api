package com.AAA.e_commerce.payment.service;

import com.AAA.e_commerce.order.model.Order;
import com.AAA.e_commerce.order.model.OrderStatus;
import com.AAA.e_commerce.order.repository.OrderRepository;
import com.AAA.e_commerce.payment.dto.PaystackInitializeRequestDto;
import com.AAA.e_commerce.payment.dto.PaystackInitializeResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaystackPaymentServiceImpl implements PaymentService {
    @Value("${paystack.secret-key}")
    private String paystackSecretKey;

    private final OrderRepository orderRepository;
    private final RestClient paystackRestClient;
    private final ObjectMapper objectMapper;

    @Override
    public String initializePayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        validateOrder(order);

        String amountInKobo = order.getTotalAmount().multiply(new BigDecimal("100")).toBigInteger().toString();
        String userEmail = order.getUser().getEmail();

        PaystackInitializeRequestDto requestPayload = new PaystackInitializeRequestDto(
                userEmail,
                amountInKobo,
                order.getTransactionReference()
        );

        PaystackInitializeResponseDto response = paystackRestClient.post()
                .uri("/transaction/initialize")
                .body(requestPayload)
                .retrieve()
                .body(PaystackInitializeResponseDto.class);

        if (response != null && response.status()) {
            return response.data().authorization_url();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to initialize Paystack payment");
        }
    }

    @Transactional
    @Override
    public void processWebhook(String payload, String signature) {
        if (signature == null || !isValidSignature(payload, signature)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Webhook Signature");
        };

        try {
            JsonNode rootNode = objectMapper.readTree(payload);

            String event = rootNode.path("event").asText();

            if ("charge.success".equals(event)){
                String reference = rootNode.path("data").path("reference").asText();

                updateOrderToPaid(reference);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing webhook payload");
        }


    }

    private void validateOrder(Order order) {
        //check if already paid
        if (order.getOrderStatus() == OrderStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is already paid");
        }
        //if cancelled or expired
        if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.EXPIRED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order has expired or been cancelled. Please place a new order.");
        }
        //has passed 15 minutes
        if (order.getExpiresAt() != null && LocalDateTime.now().isAfter(order.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment window expired. Please place a new order.");
        }
    }

    private void updateOrderToPaid(String transactionReference) {
        Order order = orderRepository.
                findByTransactionReference(transactionReference)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getOrderStatus() == OrderStatus.PAID) {
            return;
        }
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    private boolean isValidSignature(String payload, String signature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");

            SecretKeySpec secretKeySpec = new SecretKeySpec(paystackSecretKey.getBytes(), "HmacSHA512");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes());

            StringBuilder hexString = new StringBuilder();

            for(byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().equals(signature);
        } catch (Exception e) {
            return false;
        }
    }


}
