package com.liliesandlatte.app.service;

import com.liliesandlatte.app.model.OrderDetail;
import com.liliesandlatte.app.model.Orders;
import com.liliesandlatte.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Orders createOrder(Orders order) {
        // Calculate total from order details
        double total = order.getOrderDetails().stream()
                .mapToDouble(detail -> detail.getPriceAtPurchase() * detail.getQuantity())
                .sum();
        order.setTotalAmount(total);
        order.setStatus(Orders.Status.PENDING);
        
        // Set order reference in order details
        for (OrderDetail detail : order.getOrderDetails()) {
            detail.setOrder(order);
        }
        
        return orderRepository.save(order);
    }

    public List<Orders> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Orders getOrderByStripePaymentId(String stripePaymentId) {
        return orderRepository.findByStripePaymentId(stripePaymentId)
                .orElseThrow(() -> new RuntimeException("Order not found with given Stripe payment id"));
    }

    public Orders setStripePaymentId(Long orderId, String stripePaymentId) {
        Orders order = getOrderById(orderId);
        order.setStripePaymentId(stripePaymentId);
        return orderRepository.save(order);
    }

    public Orders updateStatus(Long orderId, Orders.Status status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long orderId, Long userId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        
        if (order.getStatus() == Orders.Status.PREPARING || 
            order.getStatus() == Orders.Status.READY_FOR_PICKUP) {
            throw new RuntimeException("Cannot cancel order in current status");
        }
        
        order.setStatus(Orders.Status.CANCELLED);
        orderRepository.save(order);
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<Orders> allOrders = orderRepository.findAll();
        
        long totalOrders = allOrders.size();
        double totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == Orders.Status.COMPLETED)
                .mapToDouble(Orders::getTotalAmount)
                .sum();
        
        long pendingOrders = allOrders.stream()
                .filter(o -> o.getStatus() == Orders.Status.PENDING)
                .count();
        
        long preparingOrders = allOrders.stream()
                .filter(o -> o.getStatus() == Orders.Status.PREPARING)
                .count();
        
        long readyOrders = allOrders.stream()
                .filter(o -> o.getStatus() == Orders.Status.READY_FOR_PICKUP)
                .count();
        
        stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        stats.put("pendingOrders", pendingOrders);
        stats.put("preparingOrders", preparingOrders);
        stats.put("readyOrders", readyOrders);
        
        return stats;
    }

    public List<Orders> getRecentOrders(int limit) {
        return orderRepository.findRecentOrders(org.springframework.data.domain.PageRequest.of(0, limit));
    }
}
