package com.example.bookstore.config;

import java.util.Optional;

import com.example.bookstore.model.User;
import com.example.bookstore.repository.UserRepository;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MyUserDetailService {
    
    @Inject
    UserRepository userRepository;
    
    @Transactional
    public SecurityIdentity loadUserByUsername(String username) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (!oUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        
        return QuarkusSecurityIdentity.builder()
            .setPrincipal(() -> oUser.get().getUsername())
            .addRole("SIMPLE_USER")
            .build();
    }
}