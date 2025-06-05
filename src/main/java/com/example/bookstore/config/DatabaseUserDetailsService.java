
package com.example.bookstore.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import com.example.bookstore.model.User;
import com.example.bookstore.repository.UserRepository;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DatabaseUserDetailsService {
    
    @Inject
    PasswordEncoder passwordEncoder;
    
    @Inject
    UserRepository userRepository;
    
    @Transactional
    public SecurityIdentity authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        
        if (!passwordEncoder.verify(password, user.get().getPassword())) {
            return null;
        }
        
        return QuarkusSecurityIdentity.builder()
            .setPrincipal(() -> username)
            .addRoles(new HashSet<>(Arrays.asList("")))
            .build();
    }
}