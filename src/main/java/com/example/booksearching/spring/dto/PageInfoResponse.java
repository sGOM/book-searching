package com.example.booksearching.spring.dto;

import java.util.List;

public record PageInfoResponse (
    int page,
    int size,
    int totalPages,
    long totalElements,
    boolean totalHitsRelationIsEq,
    List<Integer> barNumberList
) {

    public static PageInfoResponse of(int page, int size, int totalPages, long totalElements, boolean totalHitsRelationIsEq, List<Integer> barNumberList) {
        return new PageInfoResponse(
                page,
                size,
                totalPages,
                totalElements,
                totalHitsRelationIsEq,
                barNumberList
        );
    }

}
