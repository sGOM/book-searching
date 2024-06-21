package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatabaseException extends CustomException {

    private final DatabaseExceptionType databaseExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return DatabaseExceptionType.NOT_FOUND;
    }

}
