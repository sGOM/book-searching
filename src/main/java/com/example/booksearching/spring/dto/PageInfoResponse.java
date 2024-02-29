package com.example.booksearching.spring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record PageInfoResponse (
    @NotNull
    @PositiveOrZero
    int page,
    @NotNull
    @PositiveOrZero
    int size,
    @NotNull
    @PositiveOrZero
    int totalPages,
    @NotNull
    @PositiveOrZero
    long totalElements,
    @NotNull
    boolean totalHitsRelationIsEq,
    @NotNull
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
