package com.example.booksearching.spring.repository;

import com.example.booksearching.spring.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrdersDetail, String> {
}
