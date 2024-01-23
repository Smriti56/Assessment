package com.example.assessment.service;
import com.example.assessment.domain.Payment;
import com.example.assessment.domain.PaymentRequest;
import com.example.assessment.domain.PaymentStatus;
import com.example.assessment.domain.UserInformation;
import com.example.assessment.exception.DatabaseException;
import com.example.assessment.exception.PaymentNotFoundException;
import com.example.assessment.exception.UserNotFoundException;
import com.example.assessment.repository.PaymentRepository;
import com.example.assessment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(1L);
        paymentRequest.setPaymentAmount(100.0);
        paymentRequest.setPaymentMethod("Credit Card");
        paymentRequest.setTransactionDate("2022-01-20");
        paymentRequest.setBillingAddress("123 Main St");

        UserInformation user = new UserInformation();
        user.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(createSamplePayment());

        // Act
        Payment payment = paymentService.createPayment(paymentRequest);

        // Assert
        assertNotNull(payment);
        assertEquals("Pending", payment.getStatus());
        assertEquals("Credit Card", payment.getPaymentMethod());
        assertNotNull(payment.getTransactionId());
        assertEquals("2022-01-20", payment.getTransactionDate());
        assertEquals(user, payment.getUserInformation());
    }

    @Test
    void testCreatePaymentUserNotFound() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> paymentService.createPayment(paymentRequest));
    }

    @Test
    void testCreatePaymentDatabaseException() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserInformation()));
        when(paymentRepository.save(any(Payment.class))).thenThrow(new DataAccessException("Database error") {});

        // Act and Assert
        assertThrows(DatabaseException.class, () -> paymentService.createPayment(paymentRequest));
    }

    @Test
    void testGetPaymentStatus() {
        // Arrange
        Long userId = 1L;
        String transactionId = "abc123";

        UserInformation user = new UserInformation();
        user.setUserId(userId);

        Payment payment = createSamplePayment();
        payment.setTransactionId(transactionId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentRepository.findByTransactionId(transactionId)).thenReturn(payment);

        // Act
        PaymentStatus paymentStatus = paymentService.getPaymentStatus(userId, transactionId);

        // Assert
        assertNotNull(paymentStatus);
        assertEquals(transactionId, paymentStatus.getTransactionId());
        assertEquals("Pending", paymentStatus.getStatus());
    }

    @Test
    void testGetPaymentStatusUserNotFound() {
        // Arrange
        Long userId = 1L;
        String transactionId = "abc123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> paymentService.getPaymentStatus(userId, transactionId));
    }


    @Test
    void testStopPayment() {
        // Arrange
        Long userId = 1L;
        String transactionId = "abc123";

        UserInformation user = new UserInformation();
        user.setUserId(userId);

        Payment payment = createSamplePayment();
        payment.setTransactionId(transactionId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentRepository.findByTransactionId(transactionId)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        Payment stoppedPayment = paymentService.stopPayment(userId, transactionId);

        // Assert
        assertNotNull(stoppedPayment);
        assertEquals("Stop", stoppedPayment.getStatus());
        assertEquals(transactionId, stoppedPayment.getTransactionId());
    }

    @Test
    void testStopPaymentUserNotFound() {
        // Arrange
        Long userId = 1L;
        String transactionId = "abc123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> paymentService.stopPayment(userId, transactionId));
    }


    @Test
    void testStopPaymentDatabaseException() {
        // Arrange
        Long userId = 1L;
        String transactionId = "abc123";

        UserInformation user = new UserInformation();
        user.setUserId(userId);

        Payment payment = createSamplePayment();
        payment.setTransactionId(transactionId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentRepository.findByTransactionId(transactionId)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenThrow(new DataAccessException("Database error") {});

        // Act and Assert
        assertThrows(DatabaseException.class, () -> paymentService.stopPayment(userId, transactionId));
    }

    private Payment createSamplePayment() {
        Payment payment = new Payment();
        payment.setStatus("Pending");
        payment.setPaymentAmount(100.0);
        payment.setPaymentMethod("Credit Card");
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setTransactionDate("2022-01-20");
        payment.setBillingAddress("123 Main St");
        return payment;
    }
}