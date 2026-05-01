package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByPostAndUsername(Post post, String username);
}