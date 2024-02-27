package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TossExceptionType implements CustomExceptionType {

    TOSS_CONFIRM_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Toss 결제 최종 승인 요청이 실패했습니다.");

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
