package com.example.booksearching.spring.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ElasticsearchCommunicationException extends CustomException {

    private final ElasticsearchCommunicationExceptionType elasticsearchCommunicationExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return elasticsearchCommunicationExceptionType;
    }
}
