package com.dreiri.smarping.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.location.Location;
import android.util.Log;

public class Info {

    private Date date;
    private Location location;

    public Info(Location location) {
        setDate();
        setLocation(location);
    }

    private void setDate() {
        this.date = new Date();
    }

    private void setLocation(Location location) {
        this.location = location;
    }

    public String formatDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss' 'Z", Locale.US);
        return simpleDateFormat.format(date.getTime());
    }

    public String formatLocation() {
        if (location != null) {
            return location.toString();
        } else {
            return "null";
        }
    }

    public void log() {
        Log.i("Smarping", formatDate());
        Log.i("Smarping", formatLocation());
    }

}