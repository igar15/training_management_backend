package com.igar15.training_management.exceptions;

public class MyTokenExpiredException extends RuntimeException {

    public MyTokenExpiredException(String message) {
        super(message);
    }
}
