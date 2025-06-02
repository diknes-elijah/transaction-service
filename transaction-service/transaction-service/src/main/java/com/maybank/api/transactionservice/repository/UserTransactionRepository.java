package com.maybank.api.transactionservice.repository;

import com.maybank.api.transactionservice.model.UserTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {
    Page<UserTransaction> findByCustomerId(String customerId, Pageable pageable);
    Page<UserTransaction> findByAccountNumber(String accountNumber, Pageable pageable);
    Page<UserTransaction> findByDescriptionContaining(String description, Pageable pageable);
}