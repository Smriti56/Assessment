package com.example.assessment.domain;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @NotNull(message = "User Id should not be blank")
    private Long userId;
    private String paymentMethod;
    private double paymentAmount;
    private LocalDateTime transactionDate;
    private BillingAddress billingAddress;
    private String status;

}
