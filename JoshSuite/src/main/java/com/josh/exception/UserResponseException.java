package com.josh.exception;

public class UserResponseException extends RuntimeException {
    public UserResponseException(String msg) {
        super(msg);
    }
}
