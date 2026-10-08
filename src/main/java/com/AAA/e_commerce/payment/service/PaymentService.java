package com.AAA.e_commerce.payment.service;


public interface PaymentService {
    String initializePayment(Long orderId);

    void processWebhook(String payload, String signature);

}
