package com.liliesandlatte.app.service;

import com.liliesandlatte.app.model.Cart;
import com.liliesandlatte.app.model.CartItem;
import com.liliesandlatte.app.model.MenuItem;
import com.liliesandlatte.app.model.User;
import com.liliesandlatte.app.repository.CartItemRepository;
import com.liliesandlatte.app.repository.CartRepository;
import com.liliesandlatte.app.repository.MenuItemRepository;
import com.liliesandlatte.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getOrCreateCart(Long userId) {
        // Try fetching cart with items to avoid lazy loading issues
        return cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public CartItem addItemToCart(Long userId, Long menuItemId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = getOrCreateCart(userId);
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (!menuItem.isAvailable()) {
            throw new RuntimeException("Menu item is not available");
        }

        // Check if item already exists in cart
        CartItem cartItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElse(null);

        if (cartItem != null) {
            // Update quantity
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(quantity);
        }

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId) {
        // Ensure cart items and menu items are fetched
        return cartRepository.findByUserIdWithItems(userId).orElseGet(() -> getOrCreateCart(userId));
    }
}
