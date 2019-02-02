package com.rnmap_wb.activity.mapwork;

import org.osmdroid.views.MapView;
import  android.content.Context;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MyLocationHelper {
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
}
