package com.example.booksearching.spring.dto;

import com.example.booksearching.spring.entity.constant.PaymentStatus;
import com.example.booksearching.spring.entity.constant.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

// https://docs.tosspayments.com/reference#%EA%B2%B0%EC%A0%9C
// Version 2022-11-16
public record PaymentConfirmResponse (
        @NotNull
        String version,
        @NotNull
        @Size(max = 255)
        String paymentKey,
        @NotNull
        PaymentType type,
        @NotNull
        @Size(min = 6, max = 64)
        String orderId,
        @NotNull
        @Size(max = 100)
        String orderName,
        @NotNull
        @Size(max = 14)
        String mId,
        @NotNull
        String currency,
        @NotNull
        Method method,
        @NotNull
        @PositiveOrZero
        int totalAmount,
        @NotNull
        @PositiveOrZero
        int balanceAmount,
        @NotNull
        PaymentStatus status,
        @NotNull
        ZonedDateTime requestedAt,
        @NotNull
        ZonedDateTime approvedAt,
        @NotNull
        boolean useEscrow,
        @Size(max = 64)
        String lastTransactionKey,
        @NotNull
        @PositiveOrZero
        int suppliedAmount,
        @NotNull
        @PositiveOrZero
        int vat,
        @NotNull
        boolean cultureExpense,
        @NotNull
        @PositiveOrZero
        int taxFreeAmount,
        @NotNull
        @PositiveOrZero
        int taxExemptionAmount,
        Cancels cancels,
        @NotNull
        boolean isPartialCancelable,
        Card card,
        VirtualAccount virtualAccount,
        @Size(max = 50)
        String secret,
        MobilePhone mobilePhone,
        GiftCertificate giftCertificate,
        Transfer transfer,
        Receipt receipt,
        Checkout checkout,
        EasyPay easyPay,
        @NotNull
        @Size(min = 2, max = 2)
        String country,
        Failure failure,
        CashReceipt cashReceipt,
        CashReceipts[] cashReceipts,
        Discount discount
) {
    public record Cancels (
            @NotNull
            @PositiveOrZero
            int cancelAmount,
            @NotNull
            @Size(max = 200)
            String cancelReason,
            @NotNull
            @PositiveOrZero
            int taxFreeAmount,
            @NotNull
            @PositiveOrZero
            int taxExemptionAmount,
            @NotNull
            @PositiveOrZero
            int refundableAmount,
            @NotNull
            @PositiveOrZero
            int easyPayDiscountAmount,
            @NotNull
            ZonedDateTime canceledAt,
            @NotNull
            @Size(max = 64)
            String transactionKey,
            @Size(max = 200)
            String receiptKey
    ) { }

    public record Card (
            @NotNull
            @PositiveOrZero
            int amount,
            @NotNull
            String issuerCode,
            String acquirerCode,
            @NotNull
            @Size(max = 20)
            String number,
            @NotNull
            @PositiveOrZero
            int installmentPlanMonths,
            @NotNull
            @Size(max = 8)
            String approveNo,
            @NotNull
            boolean useCardPoint,
            @NotNull
            CardType cardType,
            @NotNull
            OwnerType ownerType,
            @NotNull
            AcquireStatus acquireStatus,
            @NotNull
            boolean isInterestFree,
            InterestPayer interestPayer
    ) {
        public enum CardType {
            신용,
            체크,
            기프트,
            미확인
        }

        public enum OwnerType {
            개인,
            법인,
            미확인
        }

        public enum AcquireStatus {
            READY,
            REQUESTED,
            COMPLETED,
            CANCEL_REQUESTED,
            CANCELED
        }

        public enum InterestPayer {
            BUYER,
            CARD_COMPANY,
            MERCHANT
        }
    }

    public record VirtualAccount (
            @NotNull
            AccountType accountType,
            @NotNull
            @Size(max = 20)
            String accountNumber,
            @NotNull
            String bankCode,
            @NotNull
            @Size(max = 100)
            String customerName,
            @NotNull
            ZonedDateTime dueDate,
            @NotNull
            RefundStatus refundStatus,
            @NotNull
            boolean expired,
            @NotNull
            SettlementStatus settlementStatus,
            @NotNull
            RefundReceiveAccount refundReceiveAccount
    ) {
        public record RefundReceiveAccount (
                @NotNull
                String bankCode,
                @NotNull
                String accountNumber,
                @NotNull
                String holderName
        ) { }

        public enum AccountType {
            일반,
            고정
        }

        public enum RefundStatus {
            NONE,
            PENDING,
            FAILED,
            PARTIAL_FAILED,
            COMPLETED
        }
    }

    public record MobilePhone (
            @NotNull
            String customerMobilePhone,
            @NotNull
            SettlementStatus settlementStatus,
            @NotNull
            String receiptUrl
    ) { }

    public record GiftCertificate (
            @NotNull
            @Size(max = 8)
            String approveNo,
            @NotNull
            SettlementStatus settlementStatus
    ) { }

    public record Transfer (
            @NotNull
            String bankCode,
            @NotNull
            SettlementStatus settlementStatus
    ) { }

    public record Receipt (
            @NotNull
            String url
    ) { }

    public record Checkout (
            @NotNull
            String url
    ) { }

    public record EasyPay (
            @NotNull
            String provider,
            @NotNull
            @PositiveOrZero
            int amount,
            @NotNull
            @PositiveOrZero
            int discountAmount
    ) { }

    public record Failure (
            @NotNull
            String code,
            @NotNull
            @Size(max = 510)
            String message
    ) { }

    public record CashReceipt (
            @NotNull
            CashReceiptType type,
            @NotNull
            @Size(max = 200)
            String receiptKey,
            @NotNull
            @Size(max = 9)
            String issueNumber,
            @NotNull
            String receiptUrl,
            @NotNull
            @PositiveOrZero
            int amount,
            @NotNull
            @PositiveOrZero
            int taxFreeAmount
    ) { }

    public record CashReceipts (
            @NotNull
            @Size(max = 200)
            String receiptKey,
            @NotNull
            @Size(min = 6, max = 64)
            String orderId,
            @NotNull
            @Size(max = 100)
            String orderName,
            @NotNull
            CashReceiptType type,
            @NotNull
            @Size(max = 9)
            String issueNumber,
            @NotNull
            String receiptUrl,
            @NotNull
            @Size(max = 10)
            String businessNumber,
            @NotNull
            TransactionType transactionType,
            @NotNull
            @PositiveOrZero
            int amount,
            @NotNull
            @PositiveOrZero
            int taxFreeAmount,
            @NotNull
            IssueStatus issueStatus,
            @NotNull
            Failure failure,
            @NotNull
            @Size(max = 30)
            String customerIdentityNumber,
            @NotNull
            ZonedDateTime requestedAt
    ) {
        public enum TransactionType {
            CONFIRM,
            CANCEL
        }

        public enum IssueStatus {
            IN_PROGRESS,
            COMPLETED,
            FAILED
        }
    }

    public record Discount (
            @NotNull
            @PositiveOrZero
            int amount
    ) { }

    public enum Method {
        카드,
        가상계좌,
        간편결제,
        휴대폰,
        계좌이체,
        문화상품권,
        도서문화상품권,
        게임문화상품권
    }

    public enum SettlementStatus {
        INCOMPLETED,
        COMPLETED
    }

    public enum CashReceiptType {
        소득공제,
        지출증빙
    }
}
