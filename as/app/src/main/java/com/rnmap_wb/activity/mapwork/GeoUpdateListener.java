package com.rnmap_wb.activity.mapwork;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.giants3.android.frame.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;

class GeoUpdateListener  implements LocationListener {
    private IMapController pMapController;

    public GeoUpdateListener(IMapController pMapController) {


        this.pMapController = pMapController;
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e("onLocationChanged:"+location );

        pMapController.animateTo(new GeoPoint(location.getLatitude(),(int )location.getLongitude()),10d,3000l);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.e("onStatusChanged:"+provider+status+extras);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("onProviderEnabled:"+provider);
    }

    @Override
    public void onProviderDisabled(String provider) {

        Log.e("onProviderDisabled:"+provider);

    }
}
