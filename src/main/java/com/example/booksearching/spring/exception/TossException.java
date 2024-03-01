package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TossException extends CustomException {

    private final TossExceptionType tossExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return tossExceptionType;
    }
}
