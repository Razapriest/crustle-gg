package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.Vote;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.VoteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    public PostController(PostRepository postRepository,
                          CommentRepository commentRepository,
                          VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
    }

    // CREATE POST
    @GetMapping("/post/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create-post";
    }

    @PostMapping("/post/create")
    public String createPost(@RequestParam String title,
                             @RequestParam String deckList,
                             @RequestParam String description,
                             @RequestParam String imageUrl,
                             Authentication auth) {

        Post post = new Post();
        post.setTitle(title);
        post.setDeckList(deckList);
        post.setDescription(description);
        post.setImageUrl(imageUrl);
        post.setAuthor(auth.getName());

        postRepository.save(post);

        return "redirect:/";
    }

    // VIEW POST
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id,
                           Model model,
                           Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        model.addAttribute("post", post);
        model.addAttribute("currentUser",
                auth != null ? auth.getName() : null);

        return "post";
    }

    // EDIT POST
    @PostMapping("/post/edit/{id}")
    public String editPost(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String deckList,
                           @RequestParam String extraInfo,
                           @RequestParam String imageUrl,
                           Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().equals(auth.getName())) {
            return "redirect:/?error=unauthorized";
        }

        post.setTitle(title);
        post.setDeckList(deckList);
        post.setExtraInfo(extraInfo);
        post.setImageUrl(imageUrl);

        postRepository.save(post);

        return "redirect:/post/" + id;
    }

    // DELETE POST
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id,
                             Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().equals(auth.getName())) {
            return "redirect:/?error=unauthorized";
        }

        postRepository.delete(post);

        return "redirect:/";
    }

    // =========================
    // 👍 LIKE / DISLIKE SYSTEM
    // =========================

    @PostMapping("/post/{id}/like")
    public String like(@PathVariable Long id, Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String user = auth.getName();

        Optional<Vote> existing = voteRepository.findByPostAndUsername(post, user);

        if (existing.isPresent()) {
            Vote v = existing.get();

            if (v.getValue() == 1) {
                voteRepository.delete(v);
                post.setLikes(post.getLikes() - 1);
            } else {
                v.setValue(1);
                voteRepository.save(v);

                post.setLikes(post.getLikes() + 1);
                post.setDislikes(post.getDislikes() - 1);
            }

        } else {
            Vote v = new Vote();
            v.setPost(post);
            v.setUsername(user);
            v.setValue(1);

            voteRepository.save(v);
            post.setLikes(post.getLikes() + 1);
        }

        postRepository.save(post);

        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{id}/dislike")
    public String dislike(@PathVariable Long id, Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String user = auth.getName();

        Optional<Vote> existing = voteRepository.findByPostAndUsername(post, user);

        if (existing.isPresent()) {
            Vote v = existing.get();

            if (v.getValue() == -1) {
                voteRepository.delete(v);
                post.setDislikes(post.getDislikes() - 1);
            } else {
                v.setValue(-1);
                voteRepository.save(v);

                post.setDislikes(post.getDislikes() + 1);
                post.setLikes(post.getLikes() - 1);
            }

        } else {
            Vote v = new Vote();
            v.setPost(post);
            v.setUsername(user);
            v.setValue(-1);

            voteRepository.save(v);
            post.setDislikes(post.getDislikes() + 1);
        }

        postRepository.save(post);

        return "redirect:/post/" + id;
    }

    // =========================
    // 💬 COMMENTS
    // =========================

    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Authentication auth) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(auth.getName());
        comment.setPost(post);

        commentRepository.save(comment);

        return "redirect:/post/" + id;
    }

    // DELETE COMMENT
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id,
                                Authentication auth) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().equals(auth.getName())) {
            return "redirect:/?error=unauthorized";
        }

        Long postId = comment.getPost().getId();

        commentRepository.delete(comment);

        return "redirect:/post/" + postId;
    }
}