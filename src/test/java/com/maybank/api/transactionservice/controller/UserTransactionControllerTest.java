package com.maybank.api.transactionservice.controller;

import com.maybank.api.transactionservice.dto.request.UpdateUserTransactionRequest;
import com.maybank.api.transactionservice.dto.response.UserTransactionResponse;
import com.maybank.api.transactionservice.exception.ResourceNotFoundException;
import com.maybank.api.transactionservice.service.UserTransactionService;
import com.maybank.api.transactionservice.service.impl.UserTransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTransactionControllerTest {

    @InjectMocks
    private UserTransactionController userTransactionController;

    @Mock
    private UserTransactionServiceImpl userTransactionService;

    @Test
    void testSearchByCustomerId() {
        UserTransactionResponse userTransactionResponse = new UserTransactionResponse(1L, "8872832383",
                new BigDecimal("1433.00"), "FUND TRANSFER", LocalDate.of(2025, 6, 1),
                LocalTime.of(10, 30, 25), "CID1");

        List<UserTransactionResponse> userTransactionResponses = List.of(userTransactionResponse);
        Pageable pageable = PageRequest.of(0, 10);
        when(userTransactionService.searchUserTransaction("CID1", null, null, pageable)).thenReturn(new PageImpl<>(userTransactionResponses));

        ResponseEntity<Page<UserTransactionResponse>> response = userTransactionController.searchUserTransaction("CID1", null, null, pageable);
        Assertions.assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void testSearchByAccountNumber() {
        UserTransactionResponse userTransactionResponse = new UserTransactionResponse(1L, "8872832383",
                new BigDecimal("1433.00"), "FUND TRANSFER", LocalDate.of(2025, 6, 1),
                LocalTime.of(10, 30, 25), "CID1");

        List<UserTransactionResponse> userTransactionResponses = List.of(userTransactionResponse);
        Pageable pageable = PageRequest.of(0, 10);
        when(userTransactionService.searchUserTransaction(null, "8872832383", null, pageable)).thenReturn(new PageImpl<>(userTransactionResponses));

        ResponseEntity<Page<UserTransactionResponse>> response = userTransactionController.searchUserTransaction(null, "8872832383", null, pageable);
        Assertions.assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void testSearchByDescription() {
        UserTransactionResponse userTransactionResponse = new UserTransactionResponse(1L, "8872832383",
                new BigDecimal("1433.00"), "FUND TRANSFER", LocalDate.of(2025, 6, 1),
                LocalTime.of(10, 30, 25), "CID1");

        List<UserTransactionResponse> userTransactionResponses = List.of(userTransactionResponse);
        Pageable pageable = PageRequest.of(0, 10);
        when(userTransactionService.searchUserTransaction(null, null, "TRANSFER", pageable)).thenReturn(new PageImpl<>(userTransactionResponses));

        ResponseEntity<Page<UserTransactionResponse>> response = userTransactionController.searchUserTransaction(null, null, "TRANSFER", pageable);
        Assertions.assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void testUpdateRecord_Success() {
        UserTransactionResponse userTransactionResponse = new UserTransactionResponse(1L, "8872832383",
                new BigDecimal("1433.00"), "FUND TRANSFER", LocalDate.of(2025, 6, 1),
                LocalTime.of(10, 30, 25), "CID1");

        UpdateUserTransactionRequest updateUserTransactionRequest = new UpdateUserTransactionRequest("LOAN PAYMENT");
        when(userTransactionService.updateUserTransaction(1L, updateUserTransactionRequest)).thenReturn(userTransactionResponse);

        ResponseEntity<?> response = userTransactionController.updateUserTransaction(1L, updateUserTransactionRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testUpdateRecord_NotFound() {
        UpdateUserTransactionRequest updateUserTransactionRequest = new UpdateUserTransactionRequest("LOAN PAYMENT");
        when(userTransactionService.updateUserTransaction(2L, updateUserTransactionRequest)).thenThrow(new ResourceNotFoundException(String.format("User Transaction with id=%s Not Found", 2L)));

        Exception exception = assertThrows(RuntimeException.class, () -> userTransactionController.updateUserTransaction(2L, updateUserTransactionRequest));

        String expectedMessage = "User Transaction with id=2 Not Found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}