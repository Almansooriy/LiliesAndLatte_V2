package com.liliesandlatte.app.config;

import com.liliesandlatte.app.model.MenuItem;
import com.liliesandlatte.app.model.User;
import com.liliesandlatte.app.repository.MenuItemRepository;
import com.liliesandlatte.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(MenuItemRepository menuItemRepository, 
                          UserRepository userRepository, 
                          BCryptPasswordEncoder passwordEncoder) {
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize admin user if not exists
        if (userRepository.findByEmail("admin@liliesandlatte.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("Admin User");
            admin.setEmail("admin@liliesandlatte.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setPhoneNumber("+1234567890");
            admin.setProfileImageUrl("https://via.placeholder.com/150?text=Admin");
            admin.setVisaCardNumber("1111-2222-3333-4444");
            userRepository.save(admin);
            System.out.println("Admin user created: admin@liliesandlatte.com / admin123");
        }

        // Initialize sample customer if not exists
        if (userRepository.findByEmail("customer@example.com").isEmpty()) {
            User customer = new User();
            customer.setFullName("John Doe");
            customer.setEmail("customer@example.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setRole(User.Role.CUSTOMER);
            customer.setPhoneNumber("+1987654321");
            customer.setProfileImageUrl("https://via.placeholder.com/150?text=Customer");
            customer.setVisaCardNumber("4444-3333-2222-1111");
            userRepository.save(customer);
            System.out.println("Sample customer created: customer@example.com / customer123");
        }

        // Initialize menu items if empty; otherwise ensure critical items have the expected images
        if (menuItemRepository.count() == 0) {
            createSampleMenuItems();
            System.out.println("Sample menu items created");
        } else {
            // Ensure Mocha and Cinnamon Roll image URLs are set/updated
            ensureMenuItemImage("Mocha", "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/mocha-001-8301418.jpg");
            ensureMenuItemImage("Cinnamon Roll", "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2FPhoto%2FRecipes%2F2024-11-cinnamon-rolls%2Fcinnamon-rolls-211");
        }
    }

    private void createSampleMenuItems() {
        // Coffee Section
        MenuItem latte = new MenuItem();
        latte.setName("Classic Latte");
        latte.setDescription("Smooth espresso with steamed milk and a touch of foam");
        latte.setPrice(4.50);
        latte.setCategory("Coffee");
        latte.setCalories(190);
        latte.setAvailable(true);
        latte.setImageUrl("https://images.unsplash.com/photo-1561882468-9110e03e0f78?w=400");
        menuItemRepository.save(latte);

        MenuItem cappuccino = new MenuItem();
        cappuccino.setName("Cappuccino");
        cappuccino.setDescription("Rich espresso with equal parts steamed milk and foam");
        cappuccino.setPrice(4.25);
        cappuccino.setCategory("Coffee");
        cappuccino.setCalories(120);
        cappuccino.setAvailable(true);
        cappuccino.setImageUrl("https://images.unsplash.com/photo-1572442388796-11668a67e53d?w=400");
        menuItemRepository.save(cappuccino);

        MenuItem americano = new MenuItem();
        americano.setName("Americano");
        americano.setDescription("Bold espresso with hot water");
        americano.setPrice(3.50);
        americano.setCategory("Coffee");
        americano.setCalories(10);
        americano.setAvailable(true);
        americano.setImageUrl("https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=400");
        menuItemRepository.save(americano);

        MenuItem mocha = new MenuItem();
        mocha.setName("Mocha");
        mocha.setDescription("Espresso with chocolate and steamed milk");
        mocha.setPrice(5.00);
        mocha.setCategory("Coffee");
        mocha.setCalories(290);
        mocha.setAvailable(true);
        mocha.setImageUrl("https://images.immediate.co.uk/production/volatile/sites/30/2020/08/mocha-001-8301418.jpg");
        menuItemRepository.save(mocha);

        MenuItem caramelMacchiato = new MenuItem();
        caramelMacchiato.setName("Caramel Macchiato");
        caramelMacchiato.setDescription("Vanilla steamed milk with espresso and caramel drizzle");
        caramelMacchiato.setPrice(5.25);
        caramelMacchiato.setCategory("Coffee");
        caramelMacchiato.setCalories(250);
        caramelMacchiato.setAvailable(true);
        caramelMacchiato.setImageUrl("https://images.unsplash.com/photo-1570968915860-54d5c301fa9f?w=400");
        menuItemRepository.save(caramelMacchiato);

        // Pastries Section
        MenuItem croissant = new MenuItem();
        croissant.setName("Butter Croissant");
        croissant.setDescription("Flaky, buttery French pastry");
        croissant.setPrice(3.00);
        croissant.setCategory("Pastries");
        croissant.setCalories(230);
        croissant.setAvailable(true);
        croissant.setImageUrl("https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=400");
        menuItemRepository.save(croissant);

        MenuItem muffin = new MenuItem();
        muffin.setName("Blueberry Muffin");
        muffin.setDescription("Fresh baked muffin with juicy blueberries");
        muffin.setPrice(3.50);
        muffin.setCategory("Pastries");
        muffin.setCalories(360);
        muffin.setAvailable(true);
        muffin.setImageUrl("https://images.unsplash.com/photo-1607958996333-41aef7caefaa?w=400");
        menuItemRepository.save(muffin);

        MenuItem chocolateCake = new MenuItem();
        chocolateCake.setName("Chocolate Cake");
        chocolateCake.setDescription("Rich chocolate cake with ganache frosting");
        chocolateCake.setPrice(5.50);
        chocolateCake.setCategory("Pastries");
        chocolateCake.setCalories(450);
        chocolateCake.setAvailable(true);
        chocolateCake.setImageUrl("https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400");
        menuItemRepository.save(chocolateCake);

        MenuItem cinnamonRoll = new MenuItem();
        cinnamonRoll.setName("Cinnamon Roll");
        cinnamonRoll.setDescription("Warm cinnamon roll with cream cheese frosting");
        cinnamonRoll.setPrice(4.00);
        cinnamonRoll.setCategory("Pastries");
        cinnamonRoll.setCalories(420);
        cinnamonRoll.setAvailable(true);
        cinnamonRoll.setImageUrl("https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2FPhoto%2FRecipes%2F2024-11-cinnamon-rolls%2Fcinnamon-rolls-211");
        menuItemRepository.save(cinnamonRoll);

        // Tea Section
        MenuItem greenTea = new MenuItem();
        greenTea.setName("Green Tea");
        greenTea.setDescription("Premium organic green tea");
        greenTea.setPrice(3.00);
        greenTea.setCategory("Tea");
        greenTea.setCalories(0);
        greenTea.setAvailable(true);
        greenTea.setImageUrl("https://images.unsplash.com/photo-1564890369478-c89ca6d9cde9?w=400");
        menuItemRepository.save(greenTea);

        MenuItem chaiLatte = new MenuItem();
        chaiLatte.setName("Chai Latte");
        chaiLatte.setDescription("Spiced tea with steamed milk");
        chaiLatte.setPrice(4.25);
        chaiLatte.setCategory("Tea");
        chaiLatte.setCalories(190);
        chaiLatte.setAvailable(true);
        chaiLatte.setImageUrl("https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=400");
        menuItemRepository.save(chaiLatte);

        // Sandwiches
        MenuItem turkeyClub = new MenuItem();
        turkeyClub.setName("Turkey Club Sandwich");
        turkeyClub.setDescription("Fresh turkey, bacon, lettuce, tomato on whole wheat");
        turkeyClub.setPrice(7.50);
        turkeyClub.setCategory("Sandwiches");
        turkeyClub.setCalories(520);
        turkeyClub.setAvailable(true);
        turkeyClub.setImageUrl("https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=400");
        menuItemRepository.save(turkeyClub);

        MenuItem vegetarianWrap = new MenuItem();
        vegetarianWrap.setName("Vegetarian Wrap");
        vegetarianWrap.setDescription("Grilled vegetables with hummus in a whole wheat wrap");
        vegetarianWrap.setPrice(6.50);
        vegetarianWrap.setCategory("Sandwiches");
        vegetarianWrap.setCalories(380);
        vegetarianWrap.setAvailable(true);
        vegetarianWrap.setImageUrl("https://images.unsplash.com/photo-1626700051175-6818013e1d4f?w=400");
        menuItemRepository.save(vegetarianWrap);
    }

    private void ensureMenuItemImage(String name, String imageUrl) {
        menuItemRepository.findAll().stream()
                .filter(item -> item.getName() != null && item.getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresentOrElse(item -> {
                    if (item.getImageUrl() == null || !item.getImageUrl().equals(imageUrl)) {
                        item.setImageUrl(imageUrl);
                        menuItemRepository.save(item);
                        System.out.println("Updated image URL for: " + name);
                    }
                }, () -> {
                    // If item doesn't exist, create minimal entry with provided image to avoid duplication
                    MenuItem newItem = new MenuItem();
                    newItem.setName(name);
                    newItem.setDescription(name + " (from DataInitializer)");
                    newItem.setPrice(0.0);
                    newItem.setCategory("Pastries");
                    newItem.setCalories(0);
                    newItem.setAvailable(true);
                    newItem.setImageUrl(imageUrl);
                    menuItemRepository.save(newItem);
                    System.out.println("Created placeholder menu item: " + name);
                });
    }
}
