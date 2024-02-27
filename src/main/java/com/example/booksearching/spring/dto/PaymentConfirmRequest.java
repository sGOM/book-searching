package com.example.booksearching.spring.dto;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Integer amount,
        Item[] items
) {
    public record Item(
            String id,
            Integer quantity
    ) { }
}
