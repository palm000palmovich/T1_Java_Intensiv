package com.example.T1.controllers;

import com.example.T1.dto.TransactionDto;
import com.example.T1.model.Transaction;
import com.example.T1.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<Transaction> saveTransaction(@PathVariable("id") Long accId,
                                                       @Valid @RequestBody TransactionDto transactionDto){
        logger.info("Creating transaction for account {} ", accId);
        Transaction transaction = transactionService.makeTransactions(accId, transactionDto);

        if (transaction == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(transaction);
    }

    @GetMapping(path = "/{id}")
    public Transaction getTrans(@PathVariable("id") Long id){
        return transactionService.getTransaction(id);
    }
}