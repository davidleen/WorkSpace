package com.rnmap_wb.activity.mapwork;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.giants3.android.frame.util.Log;
import com.rnmap_wb.R;

public class TrackingHelper {

    private static final int MSG_REQUEST_LOCATION = 33;
    private Handler handler;
    private Activity activity;
    private TrackingListener listener;

    private boolean tracking = false;

    private long tickTimeInSeconds;
    LocationListener locationListener;
    LocationManager myLocationManager;

    public TrackingHelper(final Activity activity, TrackingListener listener) {
        this.activity = activity;
        this.listener = listener;
        myLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.e(location);
                if (TrackingHelper.this.listener != null) {
                    TrackingHelper.this.listener.onNewTrack(location.getLatitude(), location.getLongitude());
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case MSG_REQUEST_LOCATION:
                        requestLocation(TrackingHelper.this.activity);
                        sendEmptyMessageDelayed(MSG_REQUEST_LOCATION, tickTimeInSeconds * 1000);


                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void stop() {
        tracking = false;
        myLocationManager.removeUpdates(locationListener);
        handler.removeMessages(MSG_REQUEST_LOCATION);

    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_tracking_select, null);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        View[] choices = new View[]{view.findViewById(R.id.choice1), view.findViewById(R.id.choice2), view.findViewById(R.id.choice3), view.findViewById(R.id.choice4)};

        View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.choice1:
                        tickTimeInSeconds = 30;
                        startTracking();
                        break;
                    case R.id.choice2:
                        tickTimeInSeconds = 60;
                        startTracking();
                        break;
                    case R.id.choice3:
                        tickTimeInSeconds = 180;
                        startTracking();
                        break;
                    case R.id.choice4:
                        tickTimeInSeconds = 600;
                        startTracking();
                        break;


                }
                alertDialog.dismiss();
            }
        };
        for (View v : choices) {
            v.setOnClickListener(itemListener);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void startTracking() {

        tracking = true;
        requestLocation(activity);
        handler.sendEmptyMessageDelayed(MSG_REQUEST_LOCATION, tickTimeInSeconds * 1000);

    }


    public void requestLocation(Activity activity) {


/**     * A class indicating the application criteria for selecting a location provider. Providers maybe ordered according to accuracy, power usage, ability to report altitude, speed, and bearing, and monetary cost.     */
        Criteria myCriteria = new Criteria();
        myCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = myLocationManager.getBestProvider(myCriteria, true);
        if (provider == null) {
            // Log.e(TAG, "ERROR: No location provider found!");
            return;
        } else {

            try {
                if (android.support.v4.app.ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && android.support.v4.app.ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                myLocationManager.requestLocationUpdates(provider, 500, 1, locationListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public interface TrackingListener {
        void onNewTrack(double lat, double lng);
    }

    public boolean isTracking() {
        return tracking;
    }
}
