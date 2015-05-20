package com.example.kris.compass;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by kris on 20.05.15.
 */
class MyLocationListener implements LocationListener {
    private double lat;
    private double longi;
    private OnLocationListener mLocationListener;

    public MyLocationListener(OnLocationListener locationListener) {
        mLocationListener = locationListener;
    }

    public void setOnLocationListener(OnLocationListener listener) {
        mLocationListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lat = location.getLongitude();
            longi = location.getLatitude();
            mLocationListener.onLocation(lat,longi);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public interface OnLocationListener {

        void onLocation(double lat, double longi);
    }
}