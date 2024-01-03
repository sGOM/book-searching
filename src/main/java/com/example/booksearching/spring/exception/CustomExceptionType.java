package com.example.booksearching.spring.exception;

import org.springframework.http.HttpStatus;

public interface CustomExceptionType {

    HttpStatus getHttpStatus();
    String getErrorMsg();

}
