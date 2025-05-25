package com.example.T1.mappers;

import com.example.T1.dto.TransactionDto;
import com.example.T1.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public Transaction dtoToModel(TransactionDto transactionDto){
        Transaction transaction = new Transaction();

        transaction.setValue(transactionDto.getValue());
        transaction.setTimestamp(LocalDateTime.now());

        return transaction;
    }
}