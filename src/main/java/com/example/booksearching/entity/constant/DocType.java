package com.example.booksearching.entity.constant;

import lombok.Getter;

public enum DocType {
    BOOK("도서"),
    THESIS("논문");

    @Getter
    private final String type;

    DocType(String type) { this.type = type; }

    public static DocType fromString(String typeString) {
        for (DocType docType : DocType.values()) {
            if (docType.getType().equals(typeString)) {
                return docType;
            }
        }
        throw new IllegalArgumentException("해당하는 열거형 상수를 찾을 수 없습니다: " + typeString);
    }
}
