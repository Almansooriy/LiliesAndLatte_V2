package com.liliesandlatte.app.controller.api;

import com.liliesandlatte.app.model.MenuItem;
import com.liliesandlatte.app.model.Orders;
import com.liliesandlatte.app.service.MenuService;
import com.liliesandlatte.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Orders> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        try {
            String statusStr = statusMap.get("status");
            Orders.Status status = Orders.Status.valueOf(statusStr);
            Orders updatedOrder = orderService.updateStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/menu")
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        MenuItem savedItem = menuService.addItem(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        menuItem.setId(id);
        MenuItem updatedItem = menuService.updateItem(menuItem);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = orderService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
