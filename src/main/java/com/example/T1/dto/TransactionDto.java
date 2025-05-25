package com.example.T1.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TransactionDto {
    @NotNull(message = "Сумма транзакции не может быть нулевой.")
    private Long value;

    public TransactionDto(Long value) {
        this.value = value;
    }

    public TransactionDto(){}
    
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "TransactionDto{" +
                "value=" + value +
                '}';
    }
}