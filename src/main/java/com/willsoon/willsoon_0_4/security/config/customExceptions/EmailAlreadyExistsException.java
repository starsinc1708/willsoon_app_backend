package com.willsoon.willsoon_0_4.security.config.customExceptions;

public class EmailAlreadyExistsException extends IllegalStateException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
