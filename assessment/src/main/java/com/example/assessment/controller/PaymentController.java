package com.example.assessment.controller;

import com.example.assessment.domain.Payment;
import com.example.assessment.domain.PaymentRequest;
import com.example.assessment.domain.PaymentStatus;
import com.example.assessment.service.PaymentServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentServiceImpl paymentService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        try{
            logger.info("Inside createPayment request: {}", paymentRequest);
            Payment paymentResponse = paymentService.createPayment(paymentRequest);
            logger.info("Payment created successfully. Payment ID: {}", paymentResponse.getId());
            return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
        }catch(RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{userId}/payments/{transactionId}")
    public ResponseEntity<?> getPaymentStatus(@Valid @PathVariable Long userId, @PathVariable String transactionId) {
        logger.info("Inside getPaymentStatus request for userId: {} and transactionId: {}", userId, transactionId);
        try{
            PaymentStatus payment = paymentService.getPaymentStatus(userId, transactionId);
            if (payment != null) {
                logger.info("Payment status retrieved successfully. PaymentStatus: {}", payment);
                return ResponseEntity.ok(payment);
            } else {
                logger.error("Payment not found for userId: {} and transactionId: {}", userId, transactionId);
                return ResponseEntity.notFound().build();
        }
        }catch(RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{userId}/payments/{transactionId}/stop")
    public ResponseEntity<?> stopPayment(@Valid @PathVariable Long userId, @PathVariable String transactionId) {
        logger.info("Inside stopPayment request for userId: {} and transactionId: {}", userId, transactionId);

        try {
            Payment payment = paymentService.stopPayment(userId, transactionId);

            if (payment != null) {
                logger.info("Payment stopped successfully. Payment ID: {}", payment.getId());
                return ResponseEntity.ok(payment);
            } else {
                logger.error("Payment not found for userId: {} and transactionId: {}", userId, transactionId);
                return ResponseEntity.notFound().build();
            }
        }catch(RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

