package com.example.assessment.service;

import com.example.assessment.domain.*;
import com.example.assessment.repository.PaymentRepository;
import com.example.assessment.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePayment() {
        PaymentRequest paymentRequest = createPaymentRequest();
        UserInformation user = createUser();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Payment createdPayment = paymentService.createPayment(paymentRequest);

        assertEquals("Pending", createdPayment.getStatus());
        verify(notificationService).notifyUser(createdPayment, user);
    }

    @Test
    public void testGetPaymentStatus() {
        // Mocking
        Long userId = 123L;
        String transactionId = "xyz123";
        UserInformation user = createUser();
        Payment payment = createPayment(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(paymentRepository.findByTransactionId(anyString())).thenReturn(payment);

        PaymentStatus paymentStatus = paymentService.getPaymentStatus(userId, transactionId);

        assertEquals(transactionId, paymentStatus.getTransactionId());
        assertEquals("Ongoing", paymentStatus.getStatus());
    }

    @Test
    public void testStopPayment() {
        // Mocking
        Long userId = 123L;
        String transactionId = "xyz123";
        UserInformation user = createUser();
        Payment payment = createPayment(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(paymentRepository.findByTransactionId(anyString())).thenReturn(payment);
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Payment stoppedPayment = paymentService.stopPayment(userId, transactionId);

        assertEquals("Stop", stoppedPayment.getStatus());
    }

    // Helper methods to create test data
    private PaymentRequest createPaymentRequest() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(1L);
        paymentRequest.setPaymentAmount(100.0);
        paymentRequest.setPaymentMethod("Credit Card");
        paymentRequest.setTransactionDate(LocalDateTime.now());
        paymentRequest.setBillingAddress(new BillingAddress("123 Main St", "Cityville", "CA", "12345"));
        return paymentRequest;
    }

    private UserInformation createUser() {
        UserInformation user = new UserInformation();
        user.setUserId(1L);
        user.setUserEmail("john.doe@example.com");
        return user;
    }

    private Payment createPayment(UserInformation user) {
        Payment payment = new Payment();
        payment.setStatus("Ongoing");
        payment.setPaymentAmount(100.0);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId("xyz123");
        payment.setTransactionDate(LocalDateTime.now());
        payment.setUserInformation(user);
        payment.setBillingAddress(new BillingAddress("123 Main St", "Cityville", "CA", "12345"));
        return payment;
    }
}
