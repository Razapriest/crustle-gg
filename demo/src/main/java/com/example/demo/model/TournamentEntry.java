package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tournament_entries")
public class TournamentEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;

    @Enumerated(EnumType.STRING)
    private Archetype archetype;

    private String deckLink; // optional

    private int placement;

    private String score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Post tournament;

    // getters & setters

    public Long getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Archetype getArchetype() {
        return archetype;
    }

    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
    }

    public String getDeckLink() {
        return deckLink;
    }

    public void setDeckLink(String deckLink) {
        this.deckLink = deckLink;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public Post getTournament() {
        return tournament;
    }

    public void setTournament(Post tournament) {
        this.tournament = tournament;
    }

    public String getScore() { return score; }

    public void setScore(String score) { this.score = score; }

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}