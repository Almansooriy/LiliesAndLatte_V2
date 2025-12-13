package com.liliesandlatte.app.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@ConditionalOnProperty(name = "stripe.enabled", havingValue = "true")
public class StripePaymentProcessor implements PaymentProcessor {

    public StripePaymentProcessor(@Value("${stripe.apiKey:}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    @Override
    public String processPayment(double amount, String currency, Long orderId) throws Exception {
        // Stripe uses the smallest currency unit (e.g., cents). We'll assume SAR uses minor units (Halalas: /100)
        long amountInMinor = Math.round(amount * 100);

        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setAmount(amountInMinor)
                .setCurrency(currency.toLowerCase())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build());

        // Attach orderId metadata if present
        if (orderId != null) {
            paramsBuilder.putMetadata("order_id", String.valueOf(orderId));
        }

        PaymentIntent intent = PaymentIntent.create(paramsBuilder.build());
        return intent.getId();
    }
}
