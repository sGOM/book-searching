package com.example.booksearching.spring.entity.constant;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("일반 사용자"),
    ROLE_ADMIN("관리자");

    private final String type;

    Role(String type) { this.type = type; }
}

