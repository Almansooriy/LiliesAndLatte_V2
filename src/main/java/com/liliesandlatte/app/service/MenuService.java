package com.liliesandlatte.app.service;

import com.liliesandlatte.app.model.MenuItem;
import com.liliesandlatte.app.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllItems() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> getAvailableItems() {
        return menuItemRepository.findByAvailable(true);
    }

    public List<MenuItem> getItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    public Optional<MenuItem> getItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    public MenuItem addItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateItem(MenuItem menuItem) {
        if (!menuItemRepository.existsById(menuItem.getId())) {
            throw new RuntimeException("Menu item not found");
        }
        return menuItemRepository.save(menuItem);
    }

    public void deleteItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    public void toggleAvailability(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        item.setAvailable(!item.isAvailable());
        menuItemRepository.save(item);
    }
}
