package com.maybank.api.transactionservice.service.impl;

import com.maybank.api.transactionservice.dto.request.UpdateUserTransactionRequest;
import com.maybank.api.transactionservice.dto.response.UserTransactionResponse;
import com.maybank.api.transactionservice.model.UserTransaction;
import com.maybank.api.transactionservice.repository.UserTransactionRepository;
import com.maybank.api.transactionservice.service.UserTransactionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserTransactionServiceImpl implements UserTransactionService {

    private final UserTransactionRepository userTransactionRepository;

    public UserTransactionServiceImpl(UserTransactionRepository userTransactionRepository) {
        this.userTransactionRepository = userTransactionRepository;
    }

    @Override
    public Page<UserTransactionResponse> searchUserTransaction(String customerId, String accountNumber, String description, Pageable pageable) {
        if (StringUtils.isNotBlank(customerId)) {
            Page<UserTransaction> userTransactions = userTransactionRepository.findByCustomerId(customerId, pageable);
            return mapToUserTransactionResponsePage(userTransactions);
        }
        if (StringUtils.isNotBlank(accountNumber)) {
            Page<UserTransaction> userTransactions = userTransactionRepository.findByAccountNumber(accountNumber, pageable);
            return mapToUserTransactionResponsePage(userTransactions);
        }
        if (StringUtils.isNotBlank(description)) {
            Page<UserTransaction> userTransactions = userTransactionRepository.findByDescriptionContaining(description, pageable);
            return mapToUserTransactionResponsePage(userTransactions);
        }
        Page<UserTransaction> userTransactions = userTransactionRepository.findAll(pageable);
        return mapToUserTransactionResponsePage(userTransactions);
    }

    @Override
    public UserTransactionResponse updateUserTransaction(Long id, UpdateUserTransactionRequest updateUserTransactionRequest) {
        UserTransaction existingUserTransaction = userTransactionRepository.findById(id).orElseThrow(() ->
                new RuntimeException(String.format("User Transaction with id=%s Not Found", id)));

        existingUserTransaction.setDescription(updateUserTransactionRequest.getDescription());
        return mapToUserTransactionResponse(userTransactionRepository.save(existingUserTransaction));
    }

    private Page<UserTransactionResponse> mapToUserTransactionResponsePage(Page<UserTransaction> userTransactions) {
        return userTransactions.map(this::mapToUserTransactionResponse);
    }

    private UserTransactionResponse mapToUserTransactionResponse(UserTransaction userTransaction) {
        return new UserTransactionResponse(userTransaction.getId(),
                userTransaction.getAccountNumber(), userTransaction.getTransactionAmount(), userTransaction.getDescription(),
                userTransaction.getTransactionDate(), userTransaction.getTransactionTime(), userTransaction.getCustomerId());
    }
}
