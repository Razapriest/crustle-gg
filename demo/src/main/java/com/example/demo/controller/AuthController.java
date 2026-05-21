package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "uploads/";

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
                               @RequestParam(required = false) MultipartFile profileImage) {

        // username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error";
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // =========================
        // DEFAULT ROLE
        // =========================
        user.setRole(Role.USER);

        user.setDescription("");

        // =========================
        // IMAGE UPLOAD
        // =========================

        String profilePath = "/images/default-avatar.png";

        if (profileImage != null && !profileImage.isEmpty()) {

            try {

                File dir = new File(UPLOAD_DIR);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String filename =
                        UUID.randomUUID()
                                + "_"
                                + profileImage.getOriginalFilename();

                Path path = Paths.get(UPLOAD_DIR, filename);

                Files.write(path, profileImage.getBytes());

                profilePath = "/uploads/" + filename;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        user.setProfileImagePath(profilePath);

        userRepository.save(user);

        return "redirect:/login";
    }
}