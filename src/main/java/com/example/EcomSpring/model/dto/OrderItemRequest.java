package com.example.EcomSpring.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {
}
