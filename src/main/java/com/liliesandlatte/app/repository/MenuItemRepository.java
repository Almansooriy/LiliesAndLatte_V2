package com.liliesandlatte.app.repository;

import com.liliesandlatte.app.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByAvailable(boolean available);
    List<MenuItem> findByCategory(String category);
}
