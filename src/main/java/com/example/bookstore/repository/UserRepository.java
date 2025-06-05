package com.example.bookstore.repository;

import java.util.Optional;

import com.example.bookstore.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface UserRepository extends PanacheRepository<User> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
