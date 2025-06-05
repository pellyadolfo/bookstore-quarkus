package com.example.bookstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BookService {
	
	@Inject
    BookRepository bookRepository;
	
	@Inject
    ModelMapper modelMapper;
    
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }
    
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return modelMapper.map(book, BookDTO.class);
    }
    
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        bookRepository.persist(book);
        return modelMapper.map(book, BookDTO.class);
    }
    
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        modelMapper.map(bookDTO, existingBook);
        bookRepository.persist(existingBook);
        return modelMapper.map( bookDTO, BookDTO.class);
    }
    
    public void deleteBook(Long id) {
        Book book = bookRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }
}
