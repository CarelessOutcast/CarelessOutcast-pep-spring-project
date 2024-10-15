package com.example.exception;

public class DuplicationUsernameException extends RuntimeException {
    public DuplicationUsernameException(String message){
        super(message);
    }
}
