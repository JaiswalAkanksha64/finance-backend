package com.zorvyn.finance.backend.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
