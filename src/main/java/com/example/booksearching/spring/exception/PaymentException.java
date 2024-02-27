package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentException extends CustomException {

    private final PaymentExceptionType paymentExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return paymentExceptionType;
    }
}
