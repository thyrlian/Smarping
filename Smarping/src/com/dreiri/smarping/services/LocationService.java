package com.dreiri.smarping.services;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.dreiri.smarping.exceptions.LocationServicesNotAvailable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class LocationService implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private Context context;
    private LocationClient locationClient;
    private Location location;

    public static boolean checkAvailability(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }

    public LocationService(Context context) {
        if (checkAvailability(context)) {
            this.context = context;
            this.locationClient = new LocationClient(context, this, this);
        } else {
            throw new LocationServicesNotAvailable("Location Services are not available");
        }
    }

    public void connect() {
        locationClient.connect();
    }

    public void disconnect() {
        locationClient.disconnect();
    }

    public Location getLastLocation() {
        return locationClient.getLastLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult((Activity) context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Smarping", "Failed to connect to Location Services");
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("Smarping", "Connected to Location Services");
        location = getLastLocation();
    }

    @Override
    public void onDisconnected() {
        Log.e("Smarping", "Disconnected from Location Services");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Smarping", "Location is changed");
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Smarping", String.format("Status of %1$s is changed: %2$d", provider, status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Smarping", String.format("Provider: %s is enabled", provider));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Smarping", String.format("Provider: %s is disabled", provider));
    }

}
