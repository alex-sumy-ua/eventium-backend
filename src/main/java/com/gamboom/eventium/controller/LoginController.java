package com.gamboom.eventium.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private final OAuth2AuthorizedClientService clientService;

    public LoginController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) throws IOException {
        if (principal != null) {
            // GitHub provides "name" and "login" (username)
            String name = principal.getAttribute("name");
            String username = principal.getAttribute("login");
            String email = principal.getAttribute("email");

            // Log user details
            System.out.println("User authenticated: " + username);
            System.out.println("Email: " + email);

            // Add user details to the model
            model.addAttribute("name", name != null ? name : username);
            model.addAttribute("email", email);
            return "home"; // This should match the name of your Thymeleaf template (home.html)
        } else {
            // Redirect to the login page if the user is not authenticated
            response.sendRedirect("/login");
            return null;
        }
    }

    @GetMapping("/redirect-to-app")
    public void redirectToApp(@AuthenticationPrincipal OAuth2User principal,
                              Authentication authentication,
                              HttpServletResponse response) throws IOException {
        if (principal != null && authentication != null) {
            // 🧠 Log all GitHub user attributes (for debugging)
            System.out.println("OAuth2 Attributes:");
            principal.getAttributes().forEach((k, v) -> System.out.println(k + " => " + v));

            // 🔑 Get GitHub access token
            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient("github", authentication.getName());
            String accessToken = client != null ? client.getAccessToken().getTokenValue() : null;

            // 📧 Get user email
            String email = principal.getAttribute("email");

            // 🧭 Build deep link back to Android
            if (email != null && accessToken != null) {
                String redirectUri = "eventium://auth?email=" + email + "&token=" + accessToken;
                System.out.println("Redirecting to: " + redirectUri);
                response.sendRedirect(redirectUri);
            } else {
                System.out.println("Email or token is missing.");
                response.sendRedirect("/login?error=true");
            }
        } else {
            response.sendRedirect("/login");
        }
    }

}