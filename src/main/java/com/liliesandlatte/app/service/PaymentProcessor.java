package com.liliesandlatte.app.service;

public interface PaymentProcessor {
    // Process payment for an order; returns a processor-specific payment id (e.g., Stripe PaymentIntent id)
    String processPayment(double amount, String currency, Long orderId) throws Exception;
}
