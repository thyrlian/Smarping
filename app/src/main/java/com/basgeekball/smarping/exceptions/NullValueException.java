package com.basgeekball.smarping.exceptions;

public class NullValueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NullValueException(String message) {
        super(message);
    }

}