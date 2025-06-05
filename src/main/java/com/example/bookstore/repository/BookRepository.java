// BookRepository.java
package com.example.bookstore.repository;

import java.util.Optional;

import com.example.bookstore.model.Book;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface BookRepository extends PanacheRepository<Book> {
    Optional<Book> findById(Integer bookId);
}
