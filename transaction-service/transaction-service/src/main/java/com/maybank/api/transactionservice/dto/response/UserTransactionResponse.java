package com.maybank.api.transactionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionResponse {

    private Long id;

    private String accountNumber;

    private BigDecimal transactionAmount;

    private String description;

    private LocalDate transactionDate;

    private LocalTime transactionTime;

    private String customerId;
}
