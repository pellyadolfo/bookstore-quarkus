package com.example.bookstore.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;

import com.example.bookstore.dto.CartDTO;
import com.example.bookstore.dto.CartItemDTO;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CartService {
	
	@Inject
    BookRepository bookRepository;
	
	@Inject
	CartRepository cartRepository;
	
	@Inject
	UserRepository userRepository;
	
	@Inject
    ModelMapper modelMapper;
    
    public CartDTO getCartByUserId(Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        return modelMapper.map(cart, CartDTO.class);
    }
    
    public CartDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) {
    	
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Book book = bookRepository.findByIdOptional(cartItemDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + cartItemDTO.getBookId()));
        
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> createNewCart(user));
        
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItemDTO.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(cartItemDTO.getQuantity());
            cart.getItems().add(newItem);
        }
        
        cartRepository.persist(cart);
        return modelMapper.map(cart, CartDTO.class);
    }
    
    public void removeItemFromCart(Long userId, Long bookId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        
        boolean removed = cart.getItems().removeIf(item -> item.getBook().getId().equals(bookId));
        
        if (removed) {
            cartRepository.persist(cart);
        } else {
            throw new ResourceNotFoundException("Book not found in cart with id: " + bookId);
        }
    }
    
    public void clearCart(Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        
        cart.getItems().clear();
        cartRepository.persist(cart);
    }
    
    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.persist(cart);
        return cart;
    }
}
