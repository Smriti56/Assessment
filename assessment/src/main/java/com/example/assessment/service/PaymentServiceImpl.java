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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Payment createPayment(PaymentRequest paymentRequest) {
        try {
            logger.info("Creating payment for userId: {}", paymentRequest.getUserId());

            UserInformation user = userRepository.findById(paymentRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Payment payment = new Payment();
            payment.setStatus("Pending");
            payment.setPaymentAmount(paymentRequest.getPaymentAmount());
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setTransactionDate(paymentRequest.getTransactionDate());
            payment.setUserInformation(user);
            payment.setBillingAddress(paymentRequest.getBillingAddress());
            logger.info("Payment created successfully. Payment ID: {}", payment.getId());

            // Save payment to the database
            Payment paymentResponse = paymentRepository.save(payment);
            return paymentResponse;
        } catch (DataAccessException ex) {
            // Log the exception and rethrow a more specific exception
            logger.error("Database exception during payment creation: {}", ex.getMessage());
            throw new DatabaseException("Error creating payment. Please try again later.", ex);
        }

    }

    @Override
    public PaymentStatus getPaymentStatus(Long userId, String transactionId) {
        logger.info("Retrieving payment status for userId: {} and transactionId: {}", userId, transactionId);

        UserInformation user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Payment payment = paymentRepository.findByTransactionId(transactionId);

        if (payment != null) {
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setTransactionId(transactionId);
            paymentStatus.setStatus(payment.getStatus());

            logger.info("Payment status retrieved successfully. Status: {}", payment.getStatus());

            return paymentStatus;
        } else {
            logger.error("Payment not found for userId: {} and transactionId: {}", userId, transactionId);
            throw new EntityNotFoundException("Payment with id " + transactionId + " not found");
        }
    }

    @Override
    public Payment stopPayment(Long userId, String transactionId) {
        logger.info("Stopping payment for userId: {} and transactionId: {}", userId, transactionId);

        UserInformation user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            Payment payment = paymentRepository.findByTransactionId(transactionId);

            if (payment != null) {
                payment.setStatus("Stop");
                logger.info("Payment stopped successfully. Payment ID: {}", payment.getId());

                return paymentRepository.save(payment);
            } else {
                logger.error("Payment not found for userId: {} and transactionId: {}", userId, transactionId);
                throw new PaymentNotFoundException("Payment with id " + transactionId + " not found");
            }
        } catch (DataAccessException ex) {

            logger.error("Database exception during payment stopping: {}", ex.getMessage());
            throw new DatabaseException("Error stopping payment. Please try again later.", ex);
        }
    }
}

