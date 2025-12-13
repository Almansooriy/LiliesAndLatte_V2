package com.liliesandlatte.app.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liliesandlatte.app.model.Orders;
import com.liliesandlatte.app.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StripeWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Orders order;

    @BeforeEach
    public void setup() {
        order = new Orders();
        order.setId(42L);
        order.setStripePaymentId("pi_test_123");
        order.setStatus(Orders.Status.PENDING);
        given(orderService.getOrderByStripePaymentId("pi_test_123")).willReturn(order);
    }

    @Test
    public void handlePaymentIntentSucceeded() throws Exception {
        // Mock a minimal payment intent succeeded event
        // Note: added "object":"payment_intent" so the deserializer knows what it is
        String payload = "{\"id\":\"evt_1\",\"type\":\"payment_intent.succeeded\",\"data\":{\"object\":{\"id\":\"pi_test_123\",\"object\":\"payment_intent\"}}}";

        mockMvc.perform(post("/api/webhooks/stripe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verify(orderService).updateStatus(ArgumentMatchers.eq(42L), ArgumentMatchers.eq(Orders.Status.COMPLETED));
    }
}
