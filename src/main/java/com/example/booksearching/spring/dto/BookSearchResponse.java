package com.example.booksearching.spring.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookSearchResponse(
        @NotNull
        String keyword,
        List<BookInfoResponse> booksInfo,
        PageInfoResponse pageInfo
) {

    public static BookSearchResponse of(String keyword, List<BookInfoResponse> booksInfo, PageInfoResponse pageInfo) {
        return new BookSearchResponse(keyword, booksInfo, pageInfo);
    }

}
