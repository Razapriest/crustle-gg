package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // NEWEST POSTS
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // TOP POSTS
    Page<Post> findAllByOrderByLikesDesc(Pageable pageable);

    // USER POSTS (FIXED FOR ENTITY RELATION)
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
}