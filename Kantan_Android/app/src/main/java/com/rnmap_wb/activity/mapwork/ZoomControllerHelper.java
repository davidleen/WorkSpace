package com.rnmap_wb.activity.mapwork;

import android.content.Context;
import android.view.View;

import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ZoomControllerHelper {
    private Context context;
    private MapView mapView;
    private MyLocationNewOverlay mLocationOverlay;

    public ZoomControllerHelper(Context context, MapView mapView) {
        this.context = context;

        this.mapView = mapView;
    }




    public  void setZoomAvailable( boolean on) {
        mapView.getZoomController().setVisibility(on? CustomZoomButtonsController.Visibility.ALWAYS: CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapView.setMultiTouchControls(true);
    }
}
