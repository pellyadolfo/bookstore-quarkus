package com.example.bookstore.dto;

import java.util.Set;

import lombok.Data;

@Data
public class CartDTO {
    private Long userId;
    private Set<CartItemDTO> items;
}

