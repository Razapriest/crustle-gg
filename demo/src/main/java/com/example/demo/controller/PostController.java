package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VoteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository,
                          CommentRepository commentRepository,
                          VoteRepository voteRepository,
                          UserRepository userRepository) {

        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // VIEW POST
    // =========================
    @GetMapping("/post/view/{id}")
    public String viewPost(@PathVariable Long id,
                           Model model,
                           Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String currentUser = auth != null ? auth.getName() : null;

        int userVote = 0;

        if (currentUser != null) {
            userVote = voteRepository.findByPostAndUsername(post, currentUser)
                    .map(Vote::getValue)
                    .orElse(0);
        }

        model.addAttribute("post", post);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userVote", userVote);
        model.addAttribute("archetypes", Archetype.values());

        return "post";
    }

    // =========================
    // CREATE DECK PAGE
    // =========================
    @GetMapping("/post/create-deck-post")
    public String createDeckPage(Model model) {
        model.addAttribute("archetypes", Archetype.values());
        return "create-deck-post";
    }

    // =========================
    // CREATE TOURNAMENT PAGE
    // =========================
    @GetMapping("/post/create-tournament-post")
    public String showTournamentForm(Model model) {
        model.addAttribute("archetypes", Archetype.values());
        return "create-tournament-post";
    }

    // =========================
    // CREATE DECK POST
    // =========================
    @PostMapping("/post/create-deck-post")
    public String createDeckPost(@RequestParam String title,
                                 @RequestParam String deckList,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false) String imageUrl,
                                 @RequestParam Archetype archetype,
                                 Authentication auth) {

        if (auth == null) return "redirect:/login";

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Post post = new Post();
        post.setTitle(title);
        post.setDeckList(deckList);
        post.setDescription(description);
        post.setImageUrl(imageUrl);
        post.setAuthor(user);
        post.setArchetype(archetype);
        post.setType(PostType.DECK);

        postRepository.save(post);

        return "redirect:/";
    }

    // =========================
    // CREATE TOURNAMENT POST
    // =========================
    @PostMapping("/post/create-tournament-post")
    public String createTournamentPost(
            @RequestParam String title,
            @RequestParam(required = false) String imageUrl,
            @RequestParam("playerName") List<String> playerNames,
            @RequestParam("archetype") List<Archetype> archetypes,
            @RequestParam("deckLink") List<String> deckLinks,
            @RequestParam(required = false) String description,
            Authentication auth
    ) {

        if (auth == null) return "redirect:/login";

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Post tournament = new Post();
        tournament.setTitle(title);
        tournament.setImageUrl(imageUrl);
        tournament.setDescription(description);
        tournament.setAuthor(user);
        tournament.setType(PostType.TOURNAMENT);

        int placement = 1;

        for (int i = 0; i < playerNames.size(); i++) {

            if (playerNames.get(i) == null || playerNames.get(i).isBlank()) {
                continue;
            }

            TournamentEntry entry = new TournamentEntry();
            entry.setPlayerName(playerNames.get(i));
            entry.setArchetype(archetypes.get(i));
            entry.setDeckLink(deckLinks.get(i));
            entry.setPlacement(placement++);

            tournament.addEntry(entry);
        }

        postRepository.save(tournament);

        return "redirect:/";
    }

    // =========================
    // EDIT PAGE
    // =========================
    @GetMapping("/post/edit/{id}")
    public String editPostPage(@PathVariable Long id,
                               Model model,
                               Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (auth == null || !post.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/?error=unauthorized";
        }

        model.addAttribute("post", post);
        model.addAttribute("archetypes", Archetype.values());

        return post.getType() == PostType.DECK
                ? "edit-deck-post"
                : "edit-tournament-post";
    }

    // =========================
    // EDIT POST
    // =========================
    @PostMapping("/post/edit/{id}")
    public String editPost(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam(required = false) String deckList,
            @RequestParam(required = false) Archetype postArchetype,
            @RequestParam(required = false) String extraInfo,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) String description,

            @RequestParam(required = false) List<String> playerName,
            @RequestParam(required = false) List<Archetype> archetype,
            @RequestParam(required = false) List<String> deckLink,

            Authentication auth
    ) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (auth == null || !post.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/?error=unauthorized";
        }

        post.setTitle(title);
        post.setImageUrl(imageUrl);
        post.setDescription(description);

        // =========================
        // DECK EDIT
        // =========================
        if (post.getType() == PostType.DECK) {
            post.setDeckList(deckList);
            post.setExtraInfo(extraInfo);

            if (postArchetype != null) {
                post.setArchetype(postArchetype);
            }
        }

        // =========================
        // TOURNAMENT EDIT
        // =========================
        if (post.getType() == PostType.TOURNAMENT) {

            List<TournamentEntry> updatedEntries = new ArrayList<>();

            if (playerName != null) {

                int placement = 1;

                for (int i = 0; i < playerName.size(); i++) {

                    if (playerName.get(i) == null || playerName.get(i).isBlank()) {
                        continue;
                    }

                    TournamentEntry entry = new TournamentEntry();
                    entry.setPlayerName(playerName.get(i));
                    entry.setArchetype(archetype.get(i));
                    entry.setDeckLink(deckLink.get(i));
                    entry.setPlacement(placement++);

                    updatedEntries.add(entry);
                }
            }

            // SAFE orphanRemoval-compatible update
            post.setEntries(updatedEntries);
        }

        postRepository.save(post);

        return "redirect:/post/view/" + id;
    }

    // =========================
    // DELETE POST
    // =========================
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id,
                             Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (auth == null || !post.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/?error=unauthorized";
        }

        postRepository.delete(post);

        return "redirect:/";
    }

    // =========================
    // LIKE
    // =========================
    @PostMapping("/post/{id}/like")
    public String like(@PathVariable Long id, Authentication auth) {

        if (auth == null) return "redirect:/login";

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String username = auth.getName();

        Optional<Vote> existing = voteRepository.findByPostAndUsername(post, username);

        if (existing.isPresent()) {

            Vote vote = existing.get();

            if (vote.getValue() == 1) {
                voteRepository.delete(vote);
                post.setLikes(Math.max(0, post.getLikes() - 1));

            } else {
                vote.setValue(1);
                voteRepository.save(vote);

                post.setLikes(post.getLikes() + 1);
                post.setDislikes(Math.max(0, post.getDislikes() - 1));
            }

        } else {

            Vote vote = new Vote();
            vote.setPost(post);
            vote.setUsername(username);
            vote.setValue(1);

            voteRepository.save(vote);
            post.setLikes(post.getLikes() + 1);
        }

        postRepository.save(post);

        return "redirect:/post/view/" + id;
    }

    // =========================
    // DISLIKE
    // =========================
    @PostMapping("/post/{id}/dislike")
    public String dislike(@PathVariable Long id, Authentication auth) {

        if (auth == null) return "redirect:/login";

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String username = auth.getName();

        Optional<Vote> existing = voteRepository.findByPostAndUsername(post, username);

        if (existing.isPresent()) {

            Vote vote = existing.get();

            if (vote.getValue() == -1) {
                voteRepository.delete(vote);
                post.setDislikes(Math.max(0, post.getDislikes() - 1));

            } else {
                vote.setValue(-1);
                voteRepository.save(vote);

                post.setDislikes(post.getDislikes() + 1);
                post.setLikes(Math.max(0, post.getLikes() - 1));
            }

        } else {

            Vote vote = new Vote();
            vote.setPost(post);
            vote.setUsername(username);
            vote.setValue(-1);

            voteRepository.save(vote);
            post.setDislikes(post.getDislikes() + 1);
        }

        postRepository.save(post);

        return "redirect:/post/view/" + id;
    }

    // =========================
    // ADD COMMENT
    // =========================
    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Authentication auth) {

        if (auth == null) return "redirect:/login";

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());

        post.addComment(comment);

        postRepository.save(post);

        return "redirect:/post/view/" + id;
    }

    // =========================
    // DELETE COMMENT
    // =========================
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id,
                                Authentication auth) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (auth == null ||
                !comment.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/?error=unauthorized";
        }

        Long postId = comment.getPost().getId();

        commentRepository.delete(comment);

        return "redirect:/post/view/" + postId;
    }
}