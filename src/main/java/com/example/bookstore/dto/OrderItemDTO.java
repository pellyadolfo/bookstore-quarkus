package com.example.bookstore.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long bookId;
    private Integer quantity;
}