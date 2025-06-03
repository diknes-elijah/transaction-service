package com.maybank.api.transactionservice.controller;

import com.maybank.api.transactionservice.dto.request.UpdateUserTransactionRequest;
import com.maybank.api.transactionservice.dto.response.UserTransactionResponse;
import com.maybank.api.transactionservice.service.UserTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-transaction")
public class UserTransactionController {

    private final UserTransactionService userTransactionService;

    public UserTransactionController(UserTransactionService userTransactionService) {
        this.userTransactionService = userTransactionService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserTransactionResponse>> searchUserTransaction(@RequestParam(required = false) String customerId,
                                                                               @RequestParam(required = false) String accountNumber,
                                                                               @RequestParam(required = false) String description,
                                                                               @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(userTransactionService.searchUserTransaction(customerId, accountNumber, description, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserTransactionResponse> updateUserTransaction(@PathVariable Long id, @RequestBody UpdateUserTransactionRequest updateUserTransactionRequest) {
        return ResponseEntity.ok(userTransactionService.updateUserTransaction(id, updateUserTransactionRequest));
    }
}
