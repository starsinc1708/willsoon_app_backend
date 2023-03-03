package com.willsoon.willsoon_0_4.security.config.customExceptions;

public class UsernameAlreadyExistsException extends IllegalStateException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
