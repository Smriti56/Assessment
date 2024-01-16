package com.example.assessment.domain;

import org.springframework.stereotype.Component;

@Component
public class PaymentStatus {
    public String transactionId;
    public String status;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
