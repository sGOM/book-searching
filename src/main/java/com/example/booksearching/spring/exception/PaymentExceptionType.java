package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PaymentExceptionType implements CustomExceptionType {

    PAYMENT_MISMATCH_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "결제 정보가 일치하지 않습니다."),
    PAYMENT_CONFIRM_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "결제 승인에 실패했습니다.");

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
