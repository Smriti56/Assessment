package com.example.assessment.controller;

import com.example.assessment.controller.PaymentController;
import com.example.assessment.domain.Payment;
import com.example.assessment.domain.PaymentRequest;
import com.example.assessment.domain.PaymentStatus;
import com.example.assessment.service.PaymentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PaymentControllerTest {

    @Mock
    private PaymentServiceImpl paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePayment() {
        
        PaymentRequest paymentRequest = new PaymentRequest();
        Payment mockPayment = new Payment();
        when(paymentService.createPayment(any())).thenReturn(mockPayment);

        
        ResponseEntity<?> responseEntity = paymentController.createPayment(paymentRequest);

        
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockPayment, responseEntity.getBody());
    }

    @Test
    public void testGetPaymentStatus() {
        
        Long userId = 123L;
        String transactionId = "xyz123";
        PaymentStatus mockPaymentStatus = new PaymentStatus();
        when(paymentService.getPaymentStatus(eq(userId), eq(transactionId))).thenReturn(mockPaymentStatus);

        
        ResponseEntity<?> responseEntity = paymentController.getPaymentStatus(userId, transactionId);

        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockPaymentStatus, responseEntity.getBody());
    }

    @Test
    public void testStopPayment() {
        
        Long userId = 123L;
        String transactionId = "xyz123";
        Payment mockPayment = new Payment();
        when(paymentService.stopPayment(eq(userId), eq(transactionId))).thenReturn(mockPayment);

        
        ResponseEntity<?> responseEntity = paymentController.stopPayment(userId, transactionId);

        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockPayment, responseEntity.getBody());
    }
}
