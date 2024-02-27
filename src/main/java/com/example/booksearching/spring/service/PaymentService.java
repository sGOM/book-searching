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

    // @docs https://docs.tosspayments.com/reference/using-api/api-keys
    @Value("${toss.api-key}")
    private String apiKey;

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

    public ResponseEntity<PaymentConfirmResponse> confirmPayment(PaymentConfirmRequest paymentConfirmRequest) {
        Payment payment = paymentRepository.findById(paymentConfirmRequest.paymentKey()).orElseThrow();
        Orders order = orderRepository.findById(paymentConfirmRequest.orderId()).orElseThrow();
        if (!payment.getAmount().equals(paymentConfirmRequest.amount())) {
            payment.changeStatus(PaymentStatus.ABORTED);
            throw new PaymentException(PaymentExceptionType.PAYMENT_MISMATCH_EXCEPTION);
        }

        PaymentConfirmRequest.Item[] items = paymentConfirmRequest.items();
        int totalAmount = 0;
        for (int idx = 0; idx < items.length; idx++) {
            PaymentConfirmRequest.Item item = items[idx];
            String orderDetailId = order.getId() + "-" + idx;
            Book book = bookRepository.findById(item.id()).orElseThrow();
            int amount = item.quantity() * book.getPrice();
            totalAmount += amount;

            OrdersDetail ordersDetail = OrdersDetail.of(orderDetailId, item.quantity(), amount, book, order);
            orderDetailRepository.save(ordersDetail);
            order.getOrdersDetails().add(ordersDetail);
        }

        if (order.getTotalAmount() != totalAmount) {
            payment.changeStatus(PaymentStatus.ABORTED);
            throw new PaymentException(PaymentExceptionType.PAYMENT_MISMATCH_EXCEPTION);
        }

        ResponseEntity<PaymentConfirmResponse> res = performTossConfirmation(paymentConfirmRequest);

        PaymentConfirmResponse paymentConfirmResponse = res.getBody();
        payment.changeStatus(paymentConfirmResponse.status());
        if (payment.getStatus() != PaymentStatus.DONE) {
            throw new PaymentException(PaymentExceptionType.PAYMENT_CONFIRM_FAIL);
        }
        order.changeStatus(OrderStatus.ORDER_RECEIVED);

        return res;
    }

    private ResponseEntity<PaymentConfirmResponse> performTossConfirmation(PaymentConfirmRequest paymentConfirmRequest) {
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        WebClient webClient = WebClient.create();
        String url = "https://api.tosspayments.com/v1/payments/confirm";
        String encodedApiKey = Base64.getEncoder().encodeToString((apiKey + ":").getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            return webClient.post().uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .bodyValue(paymentConfirmRequest)
                    .retrieve()
                    .toEntity(PaymentConfirmResponse.class)
                    .block();
        } catch (Exception e) {
            throw new TossException(TossExceptionType.TOSS_CONFIRM_FAIL);
        }
    }
}
