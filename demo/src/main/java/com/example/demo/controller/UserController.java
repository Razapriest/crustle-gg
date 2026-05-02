package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserController(UserRepository userRepository,
                          PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // =========================
    // PROFILE PAGE
    // =========================
    @GetMapping("/user/{username}")
    public String userProfile(@PathVariable String username,
                              Model model,
                              Authentication auth) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // FIX: use entity relation instead of string matching
        List<Post> posts = postRepository.findByAuthorOrderByCreatedAtDesc(user);

        int score = posts.stream()
                .mapToInt(Post::getLikes)
                .sum();

        String currentUser = auth != null ? auth.getName() : null;

        model.addAttribute("profileUser", user);
        model.addAttribute("posts", posts);
        model.addAttribute("score", score);
        model.addAttribute("currentUser", currentUser);

        return "profile";
    }

    // =========================
    // EDIT PROFILE PAGE
    // =========================
    @GetMapping("/user/edit/{username}")
    public String editProfileForm(@PathVariable String username,
                                  Authentication auth,
                                  Model model) {

        if (auth == null || !auth.getName().equals(username)) {
            return "redirect:/?error=unauthorized";
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        return "edit-profile";
    }

    // =========================
    // UPDATE PROFILE
    // =========================
    @PostMapping("/user/edit/{username}")
    public String editProfile(@PathVariable String username,
                              @RequestParam String description,
                              @RequestParam String profilePicture,
                              Authentication auth) {

        if (auth == null || !auth.getName().equals(username)) {
            return "redirect:/?error=unauthorized";
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // fallback image
        if (profilePicture == null || profilePicture.isBlank()) {
            profilePicture = "https://via.placeholder.com/150";
        }

        user.setDescription(description);
        user.setProfilePicture(profilePicture);

        userRepository.save(user);

        return "redirect:/user/" + username;
    }
}