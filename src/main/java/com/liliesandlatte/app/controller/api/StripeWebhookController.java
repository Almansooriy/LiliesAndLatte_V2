package com.liliesandlatte.app.controller.api;

import com.liliesandlatte.app.model.Orders;
import com.liliesandlatte.app.service.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StripeWebhookController {

    private final OrderService orderService;
    private final String webhookSecret;

    public StripeWebhookController(OrderService orderService, @Value("${stripe.webhookSecret:}") String webhookSecret) {
        this.orderService = orderService;
        this.webhookSecret = webhookSecret;
    }

    @PostMapping("/api/webhooks/stripe")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload, @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        Event event;
        try {
            if (webhookSecret != null && !webhookSecret.isBlank()) {
                event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            } else {
                // If webhook secret not configured, parse without verification (dev only)
                event = Event.GSON.fromJson(payload, Event.class);
            }
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent pi = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
            if (pi == null && event.getData() != null && event.getData().getObject() instanceof PaymentIntent) {
                pi = (PaymentIntent) event.getData().getObject();
            }
            if (pi != null) {
                String stripeId = pi.getId();
                try {
                    Orders order = orderService.getOrderByStripePaymentId(stripeId);
                    // Mark order as Completed
                    orderService.updateStatus(order.getId(), Orders.Status.COMPLETED);
                } catch (Exception ex) {
                    // Log and ignore
                }
            }
        } else if ("payment_intent.payment_failed".equals(event.getType())) {
            PaymentIntent pi = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
            if (pi == null && event.getData() != null && event.getData().getObject() instanceof PaymentIntent) {
                pi = (PaymentIntent) event.getData().getObject();
            }
            if (pi != null) {
                String stripeId = pi.getId();
                try {
                    Orders order = orderService.getOrderByStripePaymentId(stripeId);
                    orderService.updateStatus(order.getId(), Orders.Status.CANCELLED);
                } catch (Exception ex) {
                    // Log and ignore
                }
            }
        }

        return ResponseEntity.ok("received");
    }
}
