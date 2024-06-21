package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityException extends CustomException {

    private final SecurityExceptionType securityExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return securityExceptionType;
    }

}
