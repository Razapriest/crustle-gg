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
    // HELPER
    // =========================
    private boolean canEditProfile(User targetUser, User loggedUser) {

        return targetUser.getId().equals(loggedUser.getId())
                || loggedUser.isAdmin();
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

        List<Post> posts =
                postRepository.findByAuthorOrderByCreatedAtDesc(user);

        int score = posts.stream()
                .mapToInt(Post::getLikes)
                .sum();

        User loggedUser = null;

        if (auth != null) {
            loggedUser = userRepository
                    .findByUsername(auth.getName())
                    .orElse(null);
        }

        model.addAttribute("profileUser", user);
        model.addAttribute("posts", posts);
        model.addAttribute("score", score);
        model.addAttribute("loggedUser", loggedUser);

        return "profile";
    }

    // =========================
    // EDIT PROFILE PAGE
    // =========================
    @GetMapping("/user/edit/{username}")
    public String editProfileForm(@PathVariable String username,
                                  Authentication auth,
                                  Model model) {

        if (auth == null) {
            return "redirect:/login";
        }

        User loggedUser = userRepository
                .findByUsername(auth.getName())
                .orElseThrow();

        User targetUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!canEditProfile(targetUser, loggedUser)) {
            return "redirect:/?error=unauthorized";
        }

        model.addAttribute("user", targetUser);

        return "edit-profile";
    }

    // =========================
    // SAVE IMAGE
    // =========================
    private String saveImage(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        File dir = new File(UPLOAD_DIR);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName =
                UUID.randomUUID()
                        + "_"
                        + file.getOriginalFilename();

        File destination =
                new File(UPLOAD_DIR + fileName);

        file.transferTo(destination);

        return fileName;
    }

    // =========================
    // UPDATE PROFILE
    // =========================
    @PostMapping("/user/edit/{username}")
    public String editProfile(@PathVariable String username,
                              @RequestParam String description,
                              @RequestParam(required = false)
                              MultipartFile profileImage,
                              Authentication auth) throws IOException {

        if (auth == null) {
            return "redirect:/login";
        }

        User loggedUser = userRepository
                .findByUsername(auth.getName())
                .orElseThrow();

        User targetUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!canEditProfile(targetUser, loggedUser)) {
            return "redirect:/?error=unauthorized";
        }

        String uploadedImage = saveImage(profileImage);

        if (uploadedImage != null) {
            targetUser.setProfileImagePath(uploadedImage);
        }

        targetUser.setDescription(description);

        userRepository.save(targetUser);

        return "redirect:/user/" + targetUser.getUsername();
    }
}