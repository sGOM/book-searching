package com.example.booksearching.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrdersDetail {
    @Id
    @Size(min = 22, max = 24)
    private String id;

    @Positive
    @Column(nullable = false)
    private Integer quantity;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer amount;

    @OneToOne
    private Book book;

    @ManyToOne
    private Orders orders;

    private OrdersDetail(String id, Integer quantity, Integer amount, Book book, Orders orders) {
        this.id = id;
        this.quantity = quantity;
        this.amount = amount;
        this.book = book;
        this.orders = orders;
    }

    public static OrdersDetail of(String id, Integer quantity, Integer amount, Book book, Orders orders) {
        return new OrdersDetail(id, quantity, amount, book, orders);
    }
}
