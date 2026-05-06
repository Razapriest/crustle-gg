package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(nullable = false)
    private PostType type = PostType.DECK;

    // Main archetype for DECK posts
    @Enumerated(EnumType.STRING)
    private Archetype archetype;

    // For DECK posts
    @Column(length = 10000)
    private String deckList;

    // Legacy / optional
    @Column(length = 10000)
    private String tournamentDataCsv;

    @Column(length = 10000)
    private String description;

    // =========================
    // IMAGE (LOCAL FILE PATH)
    // =========================
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    private LocalDateTime createdAt;

    private String extraInfo;

    private int likes = 0;
    private int dislikes = 0;

    // =========================
    // COMMENTS
    // =========================
    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("createdAt ASC")
    private List<Comment> comments = new ArrayList<>();

    // =========================
    // TOURNAMENT ENTRIES
    // =========================
    @OneToMany(
            mappedBy = "tournament",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("placement ASC")
    private List<TournamentEntry> entries = new ArrayList<>();

    // =========================
    // TIMESTAMP
    // =========================
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // =========================
    // HELPERS
    // =========================

    public void addEntry(TournamentEntry entry) {
        entries.add(entry);
        entry.setTournament(this);
    }

    public void removeEntry(TournamentEntry entry) {
        entries.remove(entry);
        entry.setTournament(null);
    }

    public void setEntries(List<TournamentEntry> newEntries) {
        this.entries.clear();

        if (newEntries != null) {
            for (TournamentEntry entry : newEntries) {
                addEntry(entry);
            }
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public void setComments(List<Comment> newComments) {
        this.comments.clear();

        if (newComments != null) {
            for (Comment comment : newComments) {
                addComment(comment);
            }
        }
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

    public Archetype getArchetype() {
        return archetype;
    }

    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public List<Comment> getComments() {
        return comments;
    }

    public List<TournamentEntry> getEntries() {
        return entries;
    }
}