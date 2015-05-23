package com.kris.compass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private ImageView compassImage;
    private SensorEventListener mySensorEventListener;
    private Compass compass;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private LocationManager locManager;
    private LocationListener locListener;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private TextView mSpeedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup all views in activity
        mLatitudeText = (TextView) findViewById(R.id.latitudeTextView);
        mLongitudeText = (TextView) findViewById(R.id.longitudeTextView);
        mSpeedText = (TextView) findViewById(R.id.speedtextView);
        compassImage = (ImageView)findViewById(R.id.compass_back);

        setupAzimuth(); //all actions needed to receive azimuth data
    }

    private void setupAzimuth() {
        //initialize compass object
        compass = new Compass(compassImage);

        //define sensor manager and set required parameters
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (sensor != null) {
            //register listener


            //initialize listener and callback with obtained data
            mySensorEventListener = new MyAzimuthSensorListener(new MyAzimuthSensorListener.OnAzimuthListener() {
                @Override
                public void onAzimuth(float azimuthFrom, float azimuthTo) {
                    compass.rotate(azimuthFrom, azimuthTo);

                }
            });
            sensorManager.registerListener(mySensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_UI);

        } else {
            Toast.makeText(this, "ORIENTATION Sensor not found",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void setupLocation(){

        //define location manager together with providers
        locManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(this, "Exception "+ex,
                    Toast.LENGTH_LONG).show();
        }
        try {
            network_enabled = locManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(this, "Exception "+ex,
                    Toast.LENGTH_LONG).show();
        }
        if(!gps_enabled && !network_enabled){
            Toast.makeText(this, "Location is not enabled",
                    Toast.LENGTH_LONG).show();
        }

        //initialize listener and callback with obtained data
        locListener = new MyLocationListener(new MyLocationListener.OnLocationListener() {
            @Override
            public void onLocation(Location location) {
                mLatitudeText.setText(String.format("%.5f", location.getLatitude()));
                mLongitudeText.setText(String.format("%.5f", location.getLongitude()));
                mSpeedText.setText(location.getSpeed()+" km/h");
                /*Toast.makeText(getApplicationContext(), "Provider: "+location.getProvider()+" Accuracy: "+location.getAccuracy(),
                        Toast.LENGTH_LONG).show();*/
            }
        });

        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, locListener);
        }
        if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, locListener);
        }
    }

    @Override
    protected void onDestroy() {
        locManager.removeUpdates(locListener);
        locListener = null;
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLocation();
        setupAzimuth();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
