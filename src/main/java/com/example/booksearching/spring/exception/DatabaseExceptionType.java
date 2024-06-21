package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DatabaseExceptionType implements CustomExceptionType{

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 데이터가 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;

    private final String errorMsg;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
