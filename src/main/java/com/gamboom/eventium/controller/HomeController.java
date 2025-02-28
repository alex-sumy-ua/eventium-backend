package com.gamboom.eventium.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            // GitHub provides "name" and "login" (username)
            String name = principal.getAttribute("name");
            String username = principal.getAttribute("login");
            String email = principal.getAttribute("email");

            // Add user details to the model
            model.addAttribute("name", name != null ? name : username);
            model.addAttribute("email", email);
        }
        return "home"; // This should match the name of your Thymeleaf template (home.html)
    }
}