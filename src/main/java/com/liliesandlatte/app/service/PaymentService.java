package com.liliesandlatte.app.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService implements PaymentProcessor {
    // Retain the mock processor footprint for existing behavior
    public String processPayment(double amount, String currency, Long orderId) {
        return "MOCK_PAY_" + UUID.randomUUID().toString();
    }
}
