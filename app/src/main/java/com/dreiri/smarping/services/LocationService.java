package com.dreiri.smarping.services;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.dreiri.smarping.exceptions.LocationServicesNotAvailableException;
import com.dreiri.smarping.utils.ResultCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static long UPDATE_INTERVAL = 10000;
    private final static long FASTEST_UPDATE_INTERVAL = 5000;
    private Context context;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private ResultCallback<Location> callback = null;

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
            googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
            setLocationRequest();
        } else {
            throw new LocationServicesNotAvailableException("Location Services are not available");
        }
    }

    public LocationService(Context context, ResultCallback<Location> callback) {
        this(context);
        this.callback = callback;
    }

    private void setLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
    }

    public void connect() {
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    public void requestLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    public void requestLocationUpdatesAndDisconnect() {
        if (callback != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    callback.execute(location);
                    disconnect();
                }
            });
        }
    }

    public void stopUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("Smarping", "Connected to Location Services");
        requestLocationUpdatesAndDisconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Smarping", "Suspended from Location Services");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Smarping", "Location is changed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity) context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Smarping", "Failed to connect to Location Services");
        }
    }

}