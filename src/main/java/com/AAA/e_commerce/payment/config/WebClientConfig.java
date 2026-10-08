package com.AAA.e_commerce.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;



@Configuration
public class WebClientConfig {
    @Value("${paystack.base-url}")
    private String paystackBaseUrl;

    @Value("${paystack.secret-key}")
    private String paystackSecretKey;

    @Bean
    public RestClient paystackRestClient() {
        return RestClient.builder()
                .baseUrl(paystackBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + paystackSecretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}
