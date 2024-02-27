package com.example.booksearching.spring.service;

import com.example.booksearching.spring.dto.PaymentCheckRequest;
import com.example.booksearching.spring.dto.PaymentConfirmRequest;
import com.example.booksearching.spring.dto.PaymentConfirmResponse;
import com.example.booksearching.spring.entity.Book;
import com.example.booksearching.spring.entity.Orders;
import com.example.booksearching.spring.entity.OrdersDetail;
import com.example.booksearching.spring.entity.Payment;
import com.example.booksearching.spring.entity.constant.OrderStatus;
import com.example.booksearching.spring.entity.constant.PaymentStatus;
import com.example.booksearching.spring.exception.PaymentException;
import com.example.booksearching.spring.exception.PaymentExceptionType;
import com.example.booksearching.spring.exception.TossException;
import com.example.booksearching.spring.exception.TossExceptionType;
import com.example.booksearching.spring.repository.BookRepository;
import com.example.booksearching.spring.repository.OrderDetailRepository;
import com.example.booksearching.spring.repository.OrderRepository;
import com.example.booksearching.spring.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final BookRepository bookRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public void createPaymentInfo(PaymentCheckRequest paymentCheckRequest) {
        Payment payment = Payment.of(
                paymentCheckRequest.paymentKey(),
                paymentCheckRequest.amount(),
                paymentCheckRequest.paymentType(),
                PaymentStatus.READY
        );

        Orders order = Orders.of(
                paymentCheckRequest.orderId(),
                paymentCheckRequest.amount(),
                OrderStatus.READY,
                payment,
                null
        );

        paymentRepository.save(payment);
        orderRepository.save(order);
    }
}
