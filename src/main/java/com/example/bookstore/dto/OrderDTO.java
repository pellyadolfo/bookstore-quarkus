package com.example.bookstore.dto;

import java.util.Set;

import lombok.Data;

@Data
public class OrderDTO {
    private Long userId;
    private Set<OrderItemDTO> items;
}
