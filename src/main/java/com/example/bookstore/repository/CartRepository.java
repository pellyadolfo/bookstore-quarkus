package com.example.bookstore.repository;

import java.util.Optional;

import com.example.bookstore.model.Cart;
import com.example.bookstore.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface CartRepository extends PanacheRepository<Cart> {
    Optional<Cart> findByUser(User user);
}
