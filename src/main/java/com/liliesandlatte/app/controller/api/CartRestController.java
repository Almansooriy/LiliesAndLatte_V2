package com.liliesandlatte.app.controller.api;

import com.liliesandlatte.app.model.Cart;
import com.liliesandlatte.app.model.CartItem;
import com.liliesandlatte.app.service.CartService;
import com.liliesandlatte.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            Cart cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            // If unauthorized or user not found, return 401; otherwise 500
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/items")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            Long userId = getUserIdFromAuth(authentication);
            Long menuItemId = Long.valueOf(request.get("menuItemId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            CartItem cartItem = cartService.addItemToCart(userId, menuItemId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Integer> request,
            Authentication authentication) {
        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            Long userId = getUserIdFromAuth(authentication);
            Integer quantity = request.get("quantity");
            
            CartItem cartItem = cartService.updateCartItemQuantity(userId, itemId, quantity);
            return ResponseEntity.ok(cartItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable Long itemId,
            Authentication authentication) {
        try {
            if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            Long userId = getUserIdFromAuth(authentication);
            cartService.removeItemFromCart(userId, itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Long userId = getUserIdFromAuth(authentication);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Unauthorized");
        }
        String email = authentication.getName();
        com.liliesandlatte.app.model.User user = userService.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getId();
    }
}
