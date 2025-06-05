package com.example.bookstore.repository;

import java.util.List;

import com.example.bookstore.model.Order;
import com.example.bookstore.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface OrderRepository extends PanacheRepository<Order> {
    List<Order> findByUser(User user);
}