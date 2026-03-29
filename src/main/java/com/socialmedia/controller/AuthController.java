package com.socialmedia.controller;

import com.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Home redirect
    @GetMapping("/")
    public String home(Principal principal) {
        if (principal != null) {
            return "redirect:/feed";
        }
        return "redirect:/login";
    }

    // Login page
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Principal principal,
                            Model model) {
        if (principal != null) return "redirect:/feed";
        if (error != null) model.addAttribute("error", "Invalid username or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "auth/login";
    }

    // Register page
    @GetMapping("/register")
    public String registerPage(Principal principal) {
        if (principal != null) return "redirect:/feed";
        return "auth/register";
    }

    // Register form submit
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String fullName,
                           RedirectAttributes redirectAttributes) {
        try {
            userService.register(username.trim(), email.trim(), password, fullName.trim());
            redirectAttributes.addFlashAttribute("success",
                "Account created! Please log in with your credentials.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
