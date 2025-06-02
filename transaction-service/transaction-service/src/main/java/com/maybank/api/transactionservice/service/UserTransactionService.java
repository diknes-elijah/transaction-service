package com.maybank.api.transactionservice.service;

import com.maybank.api.transactionservice.dto.request.UpdateUserTransactionRequest;
import com.maybank.api.transactionservice.dto.response.UserTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserTransactionService {

    Page<UserTransactionResponse> searchUserTransaction(String customerId, String accountNumber, String description, Pageable pageable);

    UserTransactionResponse updateUserTransaction(Long id, UpdateUserTransactionRequest updateUserTransactionRequest);
}
