package com.example.demo.repository;

import com.example.demo.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // NEWEST POSTS
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // TOP POSTS (most likes)
    Page<Post> findAllByOrderByLikesDesc(Pageable pageable);
}