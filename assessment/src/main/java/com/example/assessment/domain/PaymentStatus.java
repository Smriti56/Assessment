package com.example.assessment.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PaymentStatus {

    @NotBlank(message = "Transaction Id should not be blank")
    public String transactionId;
    public String status;

}
