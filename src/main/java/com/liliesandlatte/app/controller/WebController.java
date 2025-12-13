package com.liliesandlatte.app.controller;

import com.liliesandlatte.app.model.User;
import com.liliesandlatte.app.service.MenuService;
import com.liliesandlatte.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final MenuService menuService;
    private final UserService userService;

    public WebController(MenuService menuService, UserService userService) {
        this.menuService = menuService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        try {
            userService.registerNewUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            return "redirect:/register?error=" + e.getMessage();
        }
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("menuItems", menuService.getAllItems());
        return "menu";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/profile")
    public String profile(Model model, java.security.Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User formUser, java.security.Principal principal, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        User current = userService.findByEmail(principal.getName());
        // Only allow updating profile-specific fields
        current.setFullName(formUser.getFullName());
        current.setPhoneNumber(formUser.getPhoneNumber());
        current.setProfileImageUrl(formUser.getProfileImageUrl());
        current.setVisaCardNumber(formUser.getVisaCardNumber());
        userService.updateUserProfile(current);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        return "redirect:/profile";
    }
}
