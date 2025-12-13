package com.liliesandlatte.app.controller;

import com.liliesandlatte.app.model.User;
import com.liliesandlatte.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            if (user != null) {
                model.addAttribute("currentUser", user);
            }
        }
    }
}
