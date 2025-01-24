package com.example.exception;

public class UsernameConflictException extends RuntimeException{

    public UsernameConflictException(String msg) {
        super(msg);
    }
    
}
