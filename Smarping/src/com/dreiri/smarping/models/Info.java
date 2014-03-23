package com.dreiri.smarping.models;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class Info {

    Date date;
    LatLng latLng;

    public Info() {
        this.date = getCurrentDate();
    }

    public Info(Date date) {
        this.date = date;
    }

    public Info(LatLng latLng) {
        this.date = getCurrentDate();
        this.latLng = latLng;
    }

    public Info(Date date, LatLng latLng) {
        this.date = date;
        this.latLng = latLng;
    }

    private Date getCurrentDate() {
        return new Date();
    }

}