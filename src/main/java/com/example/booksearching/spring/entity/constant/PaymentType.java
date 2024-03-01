package com.example.booksearching.spring.entity.constant;

import lombok.Getter;

@Getter
public enum PaymentType {
    NORMAL("일반결제"),
    BILLING("자동결제"),
    BRANDPAY("브랜드페이");

    private final String type;

    PaymentType(String type) { this.type = type; }
}
