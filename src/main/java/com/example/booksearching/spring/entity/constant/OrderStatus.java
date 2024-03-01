package com.example.booksearching.spring.entity.constant;

import lombok.Getter;

@Getter
public enum OrderStatus {
    READY("접수 대기"),
    ORDER_RECEIVED("주문 접수"),
    PREPARING_SHIPPING("배송 준비중"),
    START_SHIPPING("배송 출발"),
    ON_SHIPPING("배송중"),
    COMPLETE("배송 완료");

    private final String type;

    OrderStatus(String type) { this.type = type; }
}
