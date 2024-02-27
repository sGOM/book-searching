package com.example.booksearching.spring.entity.constant;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    READY("인증 대기"),
    IN_PROGRESS("결제 수단 및 소유자 정보 확인 완료, 승인 대기"),
    WAITING_FOR_DEPOSIT("가상계좌 입금 대기"),
    DONE("결제 승인"),
    CANCELED("결제 취소"),
    PARTIAL_CANCELED("결제 부분 취소"),
    ABORTED("결제 실패"),
    EXPIRED("결제 유효 시간 만료");

    private final String type;

    PaymentStatus(String type) { this.type = type; }
}
