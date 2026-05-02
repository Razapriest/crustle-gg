package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(required = false) String profilePicture) {

        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        // ✅ FIX: centralized fallback (important for consistency)
        user.setProfilePicture(
                (profilePicture == null || profilePicture.isBlank())
                        ? "https://oyster.ignimgs.com/mediawiki/apis.ign.com/pokemon-black-and-white/f/f6/Pokemans_558.gif?width=396"
                        : profilePicture
        );

        // ✅ IMPORTANT ADDITION (missing in your system design)
        user.setDescription(""); // ensures profile never null later

        userRepository.save(user);

        return "redirect:/login";
    }
}