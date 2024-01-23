package com.example.assessment.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String status;
    public String paymentMethod;
    public Double paymentAmount;
    public String transactionId;
    public LocalDateTime transactionDate;
    @ManyToOne
    @JoinColumn(name = "user_information_id")
    public UserInformation userInformation;

    @Embedded
    private BillingAddress billingAddress;

}