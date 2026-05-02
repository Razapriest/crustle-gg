package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserAdvice {

    private final UserRepository userRepository;

    public GlobalUserAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("loggedUser")
    public User loggedUser(Authentication auth) {
        if (auth == null) return null;

        return userRepository.findByUsername(auth.getName())
                .orElse(null);
    }
}