package com.example.assessment.service;


import com.example.assessment.domain.Payment;
import com.example.assessment.domain.PaymentRequest;
import com.example.assessment.domain.PaymentStatus;

public interface PaymentService {
    public Payment createPayment(PaymentRequest paymentRequest);
    public PaymentStatus getPaymentStatus(Long userId, String transactionId);
    public Payment stopPayment(Long userId, String transactionId);
}
