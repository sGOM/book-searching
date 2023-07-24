package com.example.booksearching.entity.constant;

import lombok.Getter;

public enum DocType {
    BOOK("도서"),
    THESIS("논문");

    @Getter
    private final String type;

    DocType(String type) { this.type = type; }
}
