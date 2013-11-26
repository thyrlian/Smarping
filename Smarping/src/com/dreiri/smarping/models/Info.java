package com.dreiri.smarping.models;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class Info {
    
    Date date;
    LatLng latLng;
    
    public Info() {
        // get current DateTime and Location(optional)
        this.date = new Date();
    }
    
}