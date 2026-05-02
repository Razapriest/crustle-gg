package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final PostRepository postRepository;

    public HomeController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // safety fixes
        int safePage = Math.max(0, page);

        if (!sort.equals("top")) {
            sort = "new";
        }

        Pageable pageable = PageRequest.of(safePage, 5);

        Page<Post> posts;

        if (sort.equals("top")) {
            posts = postRepository.findAllByOrderByLikesDesc(pageable);
        } else {
            posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("sort", sort);

        return "home";
    }
}