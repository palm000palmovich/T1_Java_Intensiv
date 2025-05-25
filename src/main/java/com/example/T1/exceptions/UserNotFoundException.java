package com.example.T1.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long userId){
        super("Юзер с id "  + userId + " не найден");
    }
}