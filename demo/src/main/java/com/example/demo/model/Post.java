package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // =========================
    // TYPE OF POST
    // =========================
    @Enumerated(EnumType.STRING)
    private PostType type = PostType.DECK;

    @Enumerated(EnumType.STRING)
    private Archetype archetype;

    // For DECK posts → deck list text
    // For TOURNAMENT posts → optional null or unused
    @Column(length = 5000)
    private String deckList;

    // For TOURNAMENT posts → CSV-like standings data
    // For DECK posts → can stay null
    @Column(length = 5000)
    private String tournamentDataCsv;

    @Column(length = 5000)
    private String description;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    private LocalDateTime createdAt;

    private String extraInfo;

    private int likes = 0;
    private int dislikes = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TournamentEntry> entries;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // =========================
    // GETTERS / SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeckList() {
        return deckList;
    }

    public void setDeckList(String deckList) {
        this.deckList = deckList;
    }

    public String getTournamentDataCsv() {
        return tournamentDataCsv;
    }

    public void setTournamentDataCsv(String tournamentDataCsv) {
        this.tournamentDataCsv = tournamentDataCsv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Archetype getArchetype() {
        return archetype;
    }

    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
    }

    public List<TournamentEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TournamentEntry> entries) {
        this.entries = entries;
    }
}