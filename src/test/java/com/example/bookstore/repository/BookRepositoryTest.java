package com.example.bookstore.repository;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@Transactional
public class BookRepositoryTest {
		
	@Inject
	BookRepository bookRepository;
	
	@Test
	public void saveBookTest() {
		
	}
	
}
