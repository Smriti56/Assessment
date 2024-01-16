package com.example.assessment.repository;

import com.example.assessment.domain.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testFindByTransactionId() {
        Payment payment = createPayment("xyz123");
        paymentRepository.save(payment);
        Payment foundPayment = paymentRepository.findByTransactionId("xyz123");
        assertEquals("xyz123", foundPayment.getTransactionId());
    }

    private Payment createPayment(String transactionId) {
        Payment payment = new Payment();
        payment.setStatus("Pending");
        payment.setPaymentAmount(100.0);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId(transactionId);
        payment.setTransactionDate(LocalDateTime.now());
        return payment;
    }
}
