package com.example.exception;

public class UnauthorizedAccountException extends RuntimeException {
    public UnauthorizedAccountException(String message) {
        super(message);
    }
}
