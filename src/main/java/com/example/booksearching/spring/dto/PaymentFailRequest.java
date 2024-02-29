package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.entity.constant.PaymentErrorCode;
import jakarta.validation.constraints.Size;

public record PaymentFailRequest(

    PaymentErrorCode code,
    String message,
    @Size(min = 20, max = 20)
    String orderId
) {
}
