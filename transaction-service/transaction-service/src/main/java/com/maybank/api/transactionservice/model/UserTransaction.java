package com.maybank.api.transactionservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class UserTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private BigDecimal transactionAmount;

    private String description;

    private LocalDate transactionDate;

    private LocalTime transactionTime;

    private String customerId;

    @Version
    private Long version;
}
