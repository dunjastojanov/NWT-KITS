package com.uber.rocket.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class Payment {
    @Id
    @SequenceGenerator(sequenceName = "payment_sequence", name = "payment_sequence", allocationSize = 1)
    @GeneratedValue(generator = "payment_sequence", strategy = GenerationType.AUTO)
    private Long id;

    private double amount;

    private boolean isSuccessful;

    private LocalDateTime transactionDate;

    private Long userId;

    private String paypalPaymentId;
}
