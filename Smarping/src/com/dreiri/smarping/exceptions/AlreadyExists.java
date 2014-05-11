package com.dreiri.smarping.exceptions;

public class AlreadyExists extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlreadyExists(String message) {
        super(message);
    }

}