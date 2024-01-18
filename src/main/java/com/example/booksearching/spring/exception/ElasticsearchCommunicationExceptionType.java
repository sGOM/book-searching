package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ElasticsearchCommunicationExceptionType implements CustomExceptionType {

    ELASTICSEARCH_SEARCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Elasticsearch가 요청을 처리하던 도중, 오류가 발생했습니다."),
    ELASTICSEARCH_IO_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Elasticsearch 관련 입출력 작업 중 오류가 발생했습니다.");

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
