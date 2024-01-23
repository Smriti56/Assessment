package com.example.assessment.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class BillingAddress {
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public BillingAddress() {
    }

}
