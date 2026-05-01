package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // SHOW CREATE POST PAGE
    @GetMapping("/post/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create-post";
    }

    // HANDLE POST SUBMISSION
    @PostMapping("/post/create")
    public String createPost(@RequestParam String title,
                             @RequestParam String deckList,
                             @RequestParam String description,
                             @RequestParam String imageUrl,
                             Authentication authentication) {

        Post post = new Post();
        post.setTitle(title);
        post.setDeckList(deckList);
        post.setDescription(description);
        post.setImageUrl(imageUrl);
        post.setAuthor(authentication.getName());

        postRepository.save(post);

        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        model.addAttribute("post", post);

        return "post";
    }
}