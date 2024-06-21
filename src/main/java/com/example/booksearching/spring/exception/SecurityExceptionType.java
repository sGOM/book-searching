package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SecurityExceptionType implements CustomExceptionType {

    DTO_MAPPING_FAIL(HttpStatus.BAD_REQUEST, "올바른 형식의 요청이 아닙니다."),
    INVALID_USER_INFO(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다. 아이디와 비밀번호를 확인해주세요."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.")
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
