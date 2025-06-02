package com.maybank.api.transactionservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserTransactionRequest {

    private String customerId;

    private String accountNumber;

    private String description;
}
