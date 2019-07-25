package com.rnmap_wb.activity.mapwork;

import android.content.Context;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class CompassHelper {
    private Context context;
    private MapView mapView;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;

    public CompassHelper(Context context, MapView mapView) {
        this.context = context;

        this.mapView = mapView;
    }




    public  void addCompass() {
        this.mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mapView);
        this.mCompassOverlay.enableCompass();
        mapView.getOverlays().add(this.mCompassOverlay);
    }
}
