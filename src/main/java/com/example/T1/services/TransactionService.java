package com.example.T1.services;

import com.example.T1.annotations.LogDataSourceError;
import com.example.T1.dto.TransactionDto;
import com.example.T1.exceptions.AccountNotFoundException;
import com.example.T1.exceptions.TransactionNotFoundException;
import com.example.T1.exceptions.ValueTooBigException;
import com.example.T1.mappers.TransactionMapper;
import com.example.T1.model.Account;
import com.example.T1.model.Transaction;
import com.example.T1.repository.AccountRepository;
import com.example.T1.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              TransactionMapper transactionMapper){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    @LogDataSourceError
    @Transactional
    public Transaction makeTransactions(Long accountId, TransactionDto transactionDto){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                   logger.error("Аккаунт с id " + accountId + " не найден.");
                   return new AccountNotFoundException(accountId);
                });

        if (account.getBalance() < transactionDto.getValue()){
            logger.error("На счете недостаточно средств");
            throw new ValueTooBigException();
        }

        Transaction transactionForSaving = transactionMapper.dtoToModel(transactionDto);
        account.setBalance(account.getBalance() - transactionDto.getValue());
        transactionForSaving.setAccount(account);
        transactionRepository.save(transactionForSaving);

        accountRepository.save(account);

        return transactionForSaving;
    }

    @LogDataSourceError
    public Transaction getTransaction(Long tranId){
        Transaction transaction = transactionRepository.findById(tranId)
                .orElseThrow(() -> {
                    logger.error("Транзакция с id " + tranId + " не найдена.");
                    return new TransactionNotFoundException(tranId);
                });

        return transaction;
    }
}