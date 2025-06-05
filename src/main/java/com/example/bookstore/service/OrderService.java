package com.example.bookstore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.example.bookstore.dto.CartDTO;
import com.example.bookstore.dto.CartItemDTO;
import com.example.bookstore.dto.OrderDTO;
import com.example.bookstore.exception.InsufficientStockException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.OrderStatus;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class OrderService {
	
	@Inject
    ModelMapper modelMapper;
	
	@Inject
    BookRepository bookRepository;
	
	@Inject
	CartRepository cartRepository;
	
	@Inject
	UserRepository userRepository;

	@Inject
	OrderRepository orderRepository;
	
	@Inject
	CartService cartService;
    
    public OrderDTO createOrderFromCart(Long userId) {
        System.out.print("createOrderFromCart");
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        
        if (cartDTO.getItems() == null || cartDTO.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from empty cart");
        }
        
        // Check stock availability
        for (CartItemDTO item : cartDTO.getItems()) {
            Book book = bookRepository.findByIdOptional(item.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + item.getBookId()));
            
            if (book.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for book: " + book.getTitle() + 
                        ". Available: " + book.getStockQuantity() + 
                        ", requested: " + item.getQuantity());
            }
        }
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        
        // Calculate total and create order items
        double total = 0.0;
        Set<OrderItem> orderItems = cartDTO.getItems().stream().map(cartItem -> {
            Book book = bookRepository.findByIdOptional(cartItem.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + cartItem.getBookId()));
            
            // Update book stock
            book.setStockQuantity(book.getStockQuantity() - cartItem.getQuantity());
            bookRepository.persist(book);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice());
            
            return orderItem;
        }).collect(Collectors.toSet());
        
        order.setItems(orderItems);
        total = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(total);
        
        orderRepository.persist(order);
        
        // Clear the cart after order is placed
        System.out.print("cleaning cart");
        cartService.clearCart(userId);
        System.out.print("cleant cart");
        
        return modelMapper.map(order, OrderDTO.class);
    }
    
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return modelMapper.map(order, OrderDTO.class);
    }
    
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return orderRepository.findByUser(user).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }
}
