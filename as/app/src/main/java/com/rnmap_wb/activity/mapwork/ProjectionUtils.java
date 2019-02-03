package com.rnmap_wb.activity.mapwork;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class ProjectionUtils {

    public static GeoPoint  fromScreenPixel(MapView mapView,int x, int y)
    {
        return (GeoPoint) mapView.getProjection().fromPixels(x  , y);
    }
}
