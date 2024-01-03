package com.example.booksearching.spring.exception;

public abstract class CustomException extends RuntimeException{

    public abstract CustomExceptionType getCustomExceptionType();

}
