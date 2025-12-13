package com.liliesandlatte.app.repository;

import com.liliesandlatte.app.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(Long userId);
    Optional<Orders> findByStripePaymentId(String stripePaymentId);
    
    @Query("SELECT o FROM Orders o ORDER BY o.createdAt DESC")
    List<Orders> findRecentOrders(org.springframework.data.domain.Pageable pageable);
}
