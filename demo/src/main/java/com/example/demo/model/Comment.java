package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    // ✅ FIXED: use real relation instead of String
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    private LocalDateTime createdAt;

    @ManyToOne
    private Post post;

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // 🔥 Helper method (VERY useful for Thymeleaf)
    public String getAuthorUsername() {
        return author != null ? author.getUsername() : "unknown";
    }

    public String getAuthorProfilePicture() {
        return author != null ? author.getProfilePicture() : "https://via.placeholder.com/40";
    }
}