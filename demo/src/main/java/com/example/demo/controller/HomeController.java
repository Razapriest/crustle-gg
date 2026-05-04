package com.example.demo.controller;

import com.example.demo.model.Archetype;
import com.example.demo.model.Post;
import com.example.demo.model.PostType;
import com.example.demo.repository.PostRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final PostRepository postRepository;

    public HomeController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(required = false) String archetype,
            Model model
    ) {

        List<Post> allPosts = postRepository.findAll();

        // =========================
        // FILTER DECKS
        // =========================
        List<Post> decks = allPosts.stream()
                .filter(p -> p.getType() == PostType.DECK)
                .filter(p -> archetype == null || archetype.isEmpty()
                        || (p.getArchetype() != null
                        && p.getArchetype().name().equals(archetype)))
                .collect(Collectors.toList());

        // =========================
        // FILTER TOURNAMENTS
        // =========================
        List<Post> tournaments = allPosts.stream()
                .filter(p -> p.getType() == PostType.TOURNAMENT)
                .collect(Collectors.toList());

        // =========================
        // SORT DECKS
        // =========================
        if (sort.equals("top")) {
            decks.sort((a, b) -> Integer.compare(b.getLikes(), a.getLikes()));
        } else {
            decks.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }

        // =========================
        // SORT TOURNAMENTS
        // =========================
        if (sort.equals("top")) {
            tournaments.sort((a, b) -> Integer.compare(b.getLikes(), a.getLikes()));
        } else {
            tournaments.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }

        model.addAttribute("decks", decks);
        model.addAttribute("tournaments", tournaments);
        model.addAttribute("sort", sort);
        model.addAttribute("archetype", archetype);

        return "home";
    }
}