package com.example.bookstore.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordEncoder {
    
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
    }
    
    public boolean verify(String rawPassword, String encodedPassword) {
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword).verified;
    }
}