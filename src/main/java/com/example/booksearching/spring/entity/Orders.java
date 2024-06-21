package com.example.booksearching.spring.entity;

import com.example.booksearching.spring.audit.AuditingFields;
import com.example.booksearching.spring.entity.constant.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Orders extends AuditingFields {
    @Id
    @Size(min = 20, max = 20)
    private String id;

    @Column(nullable = false)
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private OrderStatus status;

    @OneToOne
    private Payment payment;

    @OneToMany
    private List<OrdersDetail> ordersDetails;

    @ManyToOne
    private UserAccount userAccount;

    private Orders(String id, Integer totalAmount, OrderStatus status, Payment payment, List<OrdersDetail> ordersDetails) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.payment = payment;
        this.ordersDetails = ordersDetails;
    }

    public static Orders of(String id, Integer totalAmount, OrderStatus status, Payment payment, List<OrdersDetail> ordersDetails) {
        return new Orders(id, totalAmount, status, payment, ordersDetails);
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }
}
