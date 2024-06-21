package com.example.booksearching.spring.entity;

import com.example.booksearching.spring.entity.constant.PaymentStatus;
import com.example.booksearching.spring.entity.constant.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Payment {
    @Id
    @Size(max = 200)
    private String paymentKey;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private PaymentStatus status;

    private Payment(String paymentKey, Integer amount, PaymentType type, PaymentStatus status) {
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    public static Payment of(String paymentKey, Integer amount, PaymentType type, PaymentStatus status) {
        return new Payment(paymentKey, amount, type, status);
    }

    public void changeStatus(PaymentStatus status) {
        this.status = status;
    }
}
