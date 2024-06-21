package com.example.booksearching.spring.controller.api;

import com.example.booksearching.spring.dto.PaymentConfirmRequest;
import com.example.booksearching.spring.dto.PaymentConfirmResponse;
import com.example.booksearching.spring.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<PaymentConfirmResponse> confirmPayment(@RequestBody PaymentConfirmRequest paymentConfirmRequest) {
        return paymentService.confirmPayment(paymentConfirmRequest);
    }

}
