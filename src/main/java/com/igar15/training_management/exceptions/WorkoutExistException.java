package com.igar15.training_management.exceptions;

public class WorkoutExistException extends RuntimeException {

    public WorkoutExistException(String message) {
        super(message);
    }
}
