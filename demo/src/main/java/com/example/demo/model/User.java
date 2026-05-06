package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    // =========================
    // STORES FILE PATH (NOT URL)
    // Example: /uploads/user_12.png
    // =========================
    @Column(length = 500)
    private String profileImagePath;

    @Column(length = 1000)
    private String description;

    // =========================
    // GETTERS / SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // =========================
    // SAFE FALLBACK FOR UI
    // =========================
    public String getProfileImage() {
        if (profileImagePath == null || profileImagePath.isBlank()) {
            return "/images/default-avatar.png";
        }
        return profileImagePath;
    }
}