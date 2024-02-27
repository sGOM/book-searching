package com.example.booksearching.spring.entity.constant;

public enum PaymentErrorCode {
    PAY_PROCESS_CANCELED("구매자에 의해 결제가 취소되었습니다."),
    PAY_PROCESS_ABORTED("결제 진행 중 승인에 실패하여 결제가 중단되었습니다."),
    REJECT_CARD_COMPANY("카드사에 결제 승인이 거절되었습니다."),
    NOT_FOUND_PAYMENT_SESSION("결제 승인 요청에서 문제가 발생했습니다."),
    FORBIDDEN_REQUEST("요청 정보가 변경되었습니다."),
    UNAUTHORIZED_KEY("잘못된 API 키 입니다.");

    private final String type;

    PaymentErrorCode(String type) { this.type = type; }
}
