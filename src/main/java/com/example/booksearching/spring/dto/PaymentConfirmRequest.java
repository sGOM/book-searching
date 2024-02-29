package com.example.booksearching.spring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record PaymentConfirmRequest(
        @NotNull
        @Size(max = 200)
        String paymentKey,
        @NotNull
        @Size(min = 20, max = 20)
        String orderId,
        @NotNull
        @PositiveOrZero
        Integer amount,
        @NotNull
        Item[] items
) {
    public record Item(
            String id,
            @NotNull
            @Size(min = 13, max = 13)
            @NotNull
            @PositiveOrZero
            Integer quantity
    ) { }
}
