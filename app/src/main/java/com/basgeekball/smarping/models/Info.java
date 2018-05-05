package com.basgeekball.smarping.models;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.basgeekball.smarping.utils.Constants.LOG_TAG;

public class Info {

    private Date date;

    public Info() {
        setDate();
    }

    private void setDate() {
        this.date = new Date();
    }

    public String formatDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss' 'Z", Locale.US);
        return simpleDateFormat.format(date.getTime());
    }

    public void log() {
        Log.i(LOG_TAG, formatDate());
    }

}