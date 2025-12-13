package com.liliesandlatte.app.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

public class StripePaymentProcessorTest {

    private StripePaymentProcessor processor;

    @BeforeEach
    public void setup() {
        processor = new StripePaymentProcessor("");
    }

    @Test
    public void testCreatePaymentIntent() throws Exception {
        MockedStatic<PaymentIntent> mocked = Mockito.mockStatic(PaymentIntent.class);
        PaymentIntent fakeIntent = new PaymentIntent();
        fakeIntent.setId("pi_test_123");
        mocked.when(() -> PaymentIntent.create(Mockito.any(PaymentIntentCreateParams.class))).thenReturn(fakeIntent);

        String id = processor.processPayment(10.0, "SAR", 123L);
        assertNotNull(id);
        assertEquals("pi_test_123", id);
        mocked.close();
    }
}
