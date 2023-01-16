package com.uber.rocket.controller;

import com.uber.rocket.dto.PaymentDTO;
import com.uber.rocket.dto.PaymentRequestDto;
import com.uber.rocket.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/payment")
public class PaymentController {
    @Autowired
    PaypalService paypalService;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(HttpServletRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(paypalService.createPayment(request));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<String> successPay(@RequestBody PaymentDTO paymentDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(paypalService.executePayment(paymentDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
