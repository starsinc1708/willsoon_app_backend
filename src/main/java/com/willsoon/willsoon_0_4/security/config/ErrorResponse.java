package com.willsoon.willsoon_0_4.security.config;

public class ErrorResponse {
    private final String errorBody;

    public ErrorResponse( String errorBody) {
        this.errorBody = errorBody;
    }

    public String getMessage() {
        return errorBody;
    }
}
