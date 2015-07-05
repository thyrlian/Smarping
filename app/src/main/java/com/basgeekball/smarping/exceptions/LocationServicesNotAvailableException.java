package com.basgeekball.smarping.exceptions;

public class LocationServicesNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LocationServicesNotAvailableException(String message) {
        super(message);
    }

}
