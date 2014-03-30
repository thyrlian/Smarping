package com.dreiri.smarping.exceptions;

public class LocationServicesNotAvailable extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public LocationServicesNotAvailable(String message) {
        super(message);
    }

}
