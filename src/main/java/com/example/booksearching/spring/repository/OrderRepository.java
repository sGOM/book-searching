package com.example.booksearching.spring.repository;

import com.example.booksearching.spring.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, String> {
}
