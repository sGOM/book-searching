package com.example.booksearching.spring.controller.web;

import com.example.booksearching.spring.dto.PaymentCheckRequest;
import com.example.booksearching.spring.dto.PaymentFailRequest;
import com.example.booksearching.spring.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payment")
@Controller
public class PaymentPageController {

    private final PaymentService paymentService;

    @GetMapping("/checkout-success")
    public String paymentRequest(PaymentCheckRequest paymentCheckRequest) {
        paymentService.createPaymentInfo(paymentCheckRequest);

        return "/checkout-success";
    }


    @GetMapping("/checkout-fail")
    public String failPayment(PaymentFailRequest paymentFailRequest, Model model) {
        model.addAttribute("code", paymentFailRequest.code());
        model.addAttribute("message", paymentFailRequest.message());

        return "/checkout-fail";
    }
}