package com.example.booksearching.spring.dto;

import java.util.List;

public record BookSearchResponse(
        List<BookInfoResponse> booksInfo,
        PageInfoResponse pageInfo
) {

    public static BookSearchResponse of(List<BookInfoResponse> booksInfo, PageInfoResponse pageInfo) {
        return new BookSearchResponse(booksInfo, pageInfo);
    }

}
