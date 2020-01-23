package com.example.demo.domain.exception;

public class BusinessException extends RuntimeException {

    private final Object[] arguments;

    public BusinessException(String message) {
        super(message);
        arguments = new Object[0];
    }

    public BusinessException(String message, Object... arguments) {
        super(message);
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
