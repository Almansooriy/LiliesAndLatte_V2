package com.liliesandlatte.app.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liliesandlatte.app.model.Cart;
import com.liliesandlatte.app.model.CartItem;
import com.liliesandlatte.app.model.MenuItem;
import com.liliesandlatte.app.model.OrderDetail;
import com.liliesandlatte.app.model.Orders;
import com.liliesandlatte.app.model.User;
import com.liliesandlatte.app.service.CartService;
import com.liliesandlatte.app.service.OrderService;
import com.liliesandlatte.app.service.PaymentProcessor;
import com.liliesandlatte.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CartService cartService;

    @MockBean
    private PaymentProcessor paymentProcessor;

    @MockBean
    private OrderService orderService;

    private User user;

    @BeforeEach
    public void setup() throws Exception {
        user = new User();
        user.setId(1L);
        user.setEmail("customer@example.com");
        user.setFullName("Test Customer");
        // Setup cart
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setName("Test Coffee");
        item.setPrice(4.0);
        CartItem ci = new CartItem();
        ci.setId(1L);
        ci.setCart(cart);
        ci.setMenuItem(item);
        ci.setQuantity(2);
        cart.getCartItems().add(ci);

        given(userService.findByEmail("customer@example.com")).willReturn(user);
        given(cartService.getCartByUserId(ArgumentMatchers.eq(1L))).willReturn(cart);
        given(paymentProcessor.processPayment(ArgumentMatchers.anyDouble(), ArgumentMatchers.eq("SAR"), ArgumentMatchers.anyLong())).willReturn("MOCK_ID_123");
    }

    @Test
    @WithMockUser(username = "customer@example.com", roles = {"CUSTOMER"})
    public void testCheckoutSucceeds() throws Exception {
        Orders createdOrder = new Orders();
        createdOrder.setId(1L);
        given(orderService.createOrder(ArgumentMatchers.any(Orders.class))).willReturn(createdOrder);

        mockMvc.perform(post("/api/orders/checkout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(paymentProcessor).processPayment(ArgumentMatchers.anyDouble(), ArgumentMatchers.eq("SAR"), ArgumentMatchers.anyLong());
    }

    @Test
    @WithMockUser(username = "customer@example.com", roles = {"CUSTOMER"})
    public void testCheckoutPaymentFails() throws Exception {
        given(orderService.createOrder(ArgumentMatchers.any(Orders.class))).willThrow(new RuntimeException("Payment error"));
        // Simulate payment processor throwing
        given(paymentProcessor.processPayment(ArgumentMatchers.anyDouble(), ArgumentMatchers.eq("SAR"), ArgumentMatchers.anyLong())).willThrow(new RuntimeException("Stripe failure"));

        mockMvc.perform(post("/api/orders/checkout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPaymentRequired());
    }
}
