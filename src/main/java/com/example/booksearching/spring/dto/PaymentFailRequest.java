package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.entity.constant.PaymentErrorCode;

public record PaymentFailRequest(
    PaymentErrorCode code,
    String message,
    String orderId
) {
}
