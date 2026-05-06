package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/";

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
    // SAVE IMAGE
    // =========================
    private String saveImage(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) return null;

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destination = new File(UPLOAD_DIR + fileName);

        file.transferTo(destination);

        return fileName;
    }

    // =========================
    // UPDATE PROFILE
    // =========================
    @PostMapping("/user/edit/{username}")
    public String editProfile(@PathVariable String username,
                              @RequestParam String description,
                              @RequestParam(required = false) MultipartFile profileImage,
                              Authentication auth) throws IOException {

        if (auth == null || !auth.getName().equals(username)) {
            return "redirect:/?error=unauthorized";
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String uploadedImage = saveImage(profileImage);

        if (uploadedImage != null) {
            user.setProfileImagePath(uploadedImage);
        }

        user.setDescription(description);

        userRepository.save(user);

        return "redirect:/user/" + username;
    }
}