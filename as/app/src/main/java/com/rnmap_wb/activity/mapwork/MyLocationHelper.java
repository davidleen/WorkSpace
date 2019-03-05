package com.rnmap_wb.activity.mapwork;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.Manifest;
import android.app.Activity;
import  android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v13.app.ActivityCompat;
import android.util.Log;

import com.giants3.android.ToastHelper;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MyLocationHelper {
    private static final String TAG ="MyLocationHelper" ;
    private Context context;
    private MapView mapView;
    private MyLocationNewOverlay mLocationOverlay;

    public MyLocationHelper(Context context,MapView mapView) {
        this.context = context;

        this.mapView = mapView;
    }




    public  void addMyLocation() {
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mapView);
        this.mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(this.mLocationOverlay);
        mLocationOverlay.enableMyLocation();
    }


    public  void getLocation(Activity activity ) {
        ToastHelper.show("定位中...");
        GeoUpdateListener myGeoUpdateListener = new GeoUpdateListener(mapView.getController());
        LocationManager    myLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
/**     * A class indicating the application criteria for selecting a location provider. Providers maybe ordered according to accuracy, power usage, ability to report altitude, speed, and bearing, and monetary cost.     */
        Criteria myCriteria = new Criteria();
        myCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = myLocationManager.getBestProvider(myCriteria, true);
        if (provider == null) {
            Log.e(TAG, "ERROR: No location provider found!");
            return;
        } else {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED && ActivityCompat                .checkSelfPermission(
                  activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=                PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            try {
                myLocationManager.requestLocationUpdates(provider, 500, 1, myGeoUpdateListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
