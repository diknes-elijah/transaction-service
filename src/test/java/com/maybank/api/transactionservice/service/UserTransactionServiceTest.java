package com.maybank.api.transactionservice.service;

import com.maybank.api.transactionservice.dto.request.UpdateUserTransactionRequest;
import com.maybank.api.transactionservice.dto.response.UserTransactionResponse;
import com.maybank.api.transactionservice.model.UserTransaction;
import com.maybank.api.transactionservice.repository.UserTransactionRepository;
import com.maybank.api.transactionservice.service.impl.UserTransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTransactionServiceTest {

    @InjectMocks
    private UserTransactionServiceImpl userTransactionService;

    @Mock
    private UserTransactionRepository userTransactionRepository;

    @Test
    void testSearchByCustomerId() {
        Pageable pageable = PageRequest.of(0, 10);

        UserTransaction userTransaction1 = new UserTransaction();
        userTransaction1.setId(1L);
        userTransaction1.setAccountNumber("8872832383");
        userTransaction1.setTransactionAmount(new BigDecimal("1433.00"));
        userTransaction1.setDescription("FUND TRANSFER");
        userTransaction1.setTransactionDate(LocalDate.of(2025, 6, 1));
        userTransaction1.setTransactionTime(LocalTime.of(10, 30, 25));
        userTransaction1.setCustomerId("CID1");

        List<UserTransaction> userTransactions = List.of(userTransaction1);
        when(userTransactionRepository.findByCustomerId("CID1", pageable)).thenReturn(new PageImpl<>(userTransactions));

        Page<UserTransactionResponse> result = userTransactionService.searchUserTransaction("CID1", null, null, pageable);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("CID1", result.getContent().get(0).getCustomerId());
        assertEquals("8872832383", result.getContent().get(0).getAccountNumber());
    }

    @Test
    void testSearchByAccountNumber() {
        Pageable pageable = PageRequest.of(0, 10);

        UserTransaction userTransaction1 = new UserTransaction();
        userTransaction1.setId(1L);
        userTransaction1.setAccountNumber("8872832383");
        userTransaction1.setTransactionAmount(new BigDecimal("1433.00"));
        userTransaction1.setDescription("FUND TRANSFER");
        userTransaction1.setTransactionDate(LocalDate.of(2025, 6, 1));
        userTransaction1.setTransactionTime(LocalTime.of(10, 30, 25));
        userTransaction1.setCustomerId("CID1");

        List<UserTransaction> userTransactions = List.of(userTransaction1);
        when(userTransactionRepository.findByAccountNumber("8872832383", pageable)).thenReturn(new PageImpl<>(userTransactions));

        Page<UserTransactionResponse> result = userTransactionService.searchUserTransaction(null, "8872832383", null, pageable);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("CID1", result.getContent().get(0).getCustomerId());
        assertEquals("8872832383", result.getContent().get(0).getAccountNumber());
    }

    @Test
    void testSearchByDescription() {
        Pageable pageable = PageRequest.of(0, 10);

        UserTransaction userTransaction1 = new UserTransaction();
        userTransaction1.setId(1L);
        userTransaction1.setAccountNumber("8872832383");
        userTransaction1.setTransactionAmount(new BigDecimal("1433.00"));
        userTransaction1.setDescription("FUND TRANSFER");
        userTransaction1.setTransactionDate(LocalDate.of(2025, 6, 1));
        userTransaction1.setTransactionTime(LocalTime.of(10, 30, 25));
        userTransaction1.setCustomerId("CID1");

        List<UserTransaction> userTransactions = List.of(userTransaction1);
        when(userTransactionRepository.findByDescriptionContaining("TRANSFER", pageable)).thenReturn(new PageImpl<>(userTransactions));

        Page<UserTransactionResponse> result = userTransactionService.searchUserTransaction(null, null, "TRANSFER", pageable);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("CID1", result.getContent().get(0).getCustomerId());
        assertEquals("8872832383", result.getContent().get(0).getAccountNumber());
    }

    @Test
    void testSearchAllUserTransaction() {
        Pageable pageable = PageRequest.of(0, 10);

        UserTransaction userTransaction1 = new UserTransaction();
        userTransaction1.setId(1L);
        userTransaction1.setAccountNumber("8872832383");
        userTransaction1.setTransactionAmount(new BigDecimal("1433.00"));
        userTransaction1.setDescription("FUND TRANSFER");
        userTransaction1.setTransactionDate(LocalDate.of(2025, 6, 1));
        userTransaction1.setTransactionTime(LocalTime.of(10, 30, 25));
        userTransaction1.setCustomerId("CID1");

        List<UserTransaction> userTransactions = List.of(userTransaction1);
        when(userTransactionRepository.findAll(pageable)).thenReturn(new PageImpl<>(userTransactions));

        Page<UserTransactionResponse> result = userTransactionService.searchUserTransaction(null, null, null, pageable);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("CID1", result.getContent().get(0).getCustomerId());
        assertEquals("8872832383", result.getContent().get(0).getAccountNumber());
    }

    @Test
    void testUpdateUserTransaction_Success() {
        UserTransaction existing = new UserTransaction();
        existing.setId(1L);
        existing.setAccountNumber("8872832383");
        existing.setTransactionAmount(new BigDecimal("1433.00"));
        existing.setDescription("FUND TRANSFER");
        existing.setTransactionDate(LocalDate.of(2025, 6, 1));
        existing.setTransactionTime(LocalTime.of(10, 30, 25));
        existing.setCustomerId("CID1");

        UserTransaction updated = new UserTransaction();
        updated.setId(1L);
        updated.setAccountNumber("8872832383");
        updated.setTransactionAmount(new BigDecimal("1433.00"));
        updated.setDescription("LOAN PAYMENT");
        updated.setTransactionDate(LocalDate.of(2025, 6, 1));
        updated.setTransactionTime(LocalTime.of(10, 30, 25));
        updated.setCustomerId("CID1");

        when(userTransactionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userTransactionRepository.save(any())).thenReturn(updated);

        UpdateUserTransactionRequest updateUserTransactionRequest = new UpdateUserTransactionRequest("LOAN PAYMENT");
        UserTransactionResponse userTransactionResponse = userTransactionService.updateUserTransaction(1L, updateUserTransactionRequest);
        assertEquals("LOAN PAYMENT", userTransactionResponse.getDescription());
    }

    @Test
    void testUpdateUserTransaction_NotFound() {
        when(userTransactionRepository.findById(2L)).thenReturn(Optional.empty());

        UpdateUserTransactionRequest updateUserTransactionRequest = new UpdateUserTransactionRequest("LOAN PAYMENT");

        Exception exception = assertThrows(RuntimeException.class, () -> userTransactionService.updateUserTransaction(2L, updateUserTransactionRequest));

        String expectedMessage = "User Transaction with id=2 Not Found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
