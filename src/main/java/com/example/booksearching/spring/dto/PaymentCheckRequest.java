package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.entity.constant.PaymentType;

public record PaymentCheckRequest(
        PaymentType paymentType,
        String orderId,
        String paymentKey,
        Integer amount
) {
}
