package com.dreiri.smarping.exceptions;

public class NullValue extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NullValue(String message) {
        super(message);
    }

}