package com.maybank.api.transactionservice.util;

import com.maybank.api.transactionservice.model.UserTransaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionFieldSetMapper implements FieldSetMapper<UserTransaction> {

    @Override
    public UserTransaction mapFieldSet(FieldSet fieldSet) {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setAccountNumber(fieldSet.readString("ACCOUNT_NUMBER"));
        userTransaction.setTransactionAmount(BigDecimal.valueOf(fieldSet.readDouble("TRX_AMOUNT")));
        userTransaction.setDescription(fieldSet.readString("DESCRIPTION"));
        userTransaction.setTransactionDate(LocalDate.parse(fieldSet.readString("TRX_DATE")));
        userTransaction.setTransactionTime(LocalTime.parse(fieldSet.readString("TRX_TIME")));
        userTransaction.setCustomerId(fieldSet.readString("CUSTOMER_ID"));
        return userTransaction;
    }
}
