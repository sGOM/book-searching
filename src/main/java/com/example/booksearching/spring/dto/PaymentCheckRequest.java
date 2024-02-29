package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.entity.constant.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record PaymentCheckRequest(
        @NotNull
        PaymentType paymentType,
        @NotNull
        @Size(min = 20, max = 20)
        String orderId,
        @NotNull
        @Size(max = 200)
        String paymentKey,
        @NotNull
        @PositiveOrZero
        Integer amount
) {
}
