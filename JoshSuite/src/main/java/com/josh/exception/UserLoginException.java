package com.josh.exception;

public class UserLoginException extends RuntimeException {
    public UserLoginException(String msg) {
        super(msg);
    }
}
