package com.liliesandlatte.app.controller.api;

import com.liliesandlatte.app.model.Orders;
import com.liliesandlatte.app.service.OrderService;
import com.liliesandlatte.app.service.CartService;
import com.liliesandlatte.app.service.UserService;
import com.liliesandlatte.app.service.PaymentProcessor;
import com.liliesandlatte.app.model.Cart;
import com.liliesandlatte.app.model.CartItem;
import com.liliesandlatte.app.model.OrderDetail;
import com.liliesandlatte.app.model.MenuItem;
import com.liliesandlatte.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentProcessor paymentService;

    @PostMapping
    public ResponseEntity<Orders> placeOrder(@RequestBody Orders order, Authentication authentication) {
        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            // Ensure we attach the user
            order.setUser(user);
            Orders createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<Orders> checkout(Authentication authentication) {
        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            User user = userService.findByEmail(authentication.getName());
            if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null || cart.getCartItems().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Orders order = new Orders();
            order.setUser(user);
            // Create order details from cart items
            for (CartItem ci : cart.getCartItems()) {
                OrderDetail od = new OrderDetail();
                od.setMenuItem(ci.getMenuItem());
                od.setPriceAtPurchase(ci.getMenuItem().getPrice());
                od.setQuantity(ci.getQuantity());
                od.setOrder(order);
                order.getOrderDetails().add(od);
            }

            // Calculate total and persist order first to attach metadata to payment
            double amount = order.getOrderDetails().stream().mapToDouble(d -> d.getPriceAtPurchase() * d.getQuantity()).sum();
            order.setTotalAmount(amount);
            order.setStatus(Orders.Status.PENDING);
            Orders created = orderService.createOrder(order);

            String paymentId;
            try {
                paymentId = paymentService.processPayment(amount, "SAR", created.getId());
            } catch (Exception ex) {
                // Log and return 402 Payment Required for payment issues
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
            }

            // Update order with stripe payment id
            orderService.setStripePaymentId(created.getId(), paymentId);
            // Clear cart after successful order
            cartService.clearCart(user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            if ("Payment error".equals(e.getMessage())) {
                 return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Orders>> getOrdersByUserId(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null || !user.getId().equals(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<Orders> orders = orderService.getUserOrders(id);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Orders>> getMyOrders(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User current = userService.findByEmail(authentication.getName());
        if (current == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<Orders> orders = orderService.getUserOrders(current.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id, Authentication authentication) {
        try {
            Orders order = orderService.getOrderById(id);
            // In production, verify the user has permission to view this order
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id, Authentication authentication) {
        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            User current = userService.findByEmail(authentication.getName());
            if (current == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            Long userId = current.getId();
            orderService.cancelOrder(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
