package com.josh.exception;

public class PageResponseException extends RuntimeException {
    public PageResponseException(String msg) {
        super(msg);
    }
}
