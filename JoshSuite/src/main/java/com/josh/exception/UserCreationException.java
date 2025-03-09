package com.josh.exception;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String msg) {
        super(msg);
    }
}
