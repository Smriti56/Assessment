package com.example.assessment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="user_info")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "User Id should not be blank")
    private Long userId;

    @NotBlank(message = "Email Id should not be blank")
    private String userEmail;

}
