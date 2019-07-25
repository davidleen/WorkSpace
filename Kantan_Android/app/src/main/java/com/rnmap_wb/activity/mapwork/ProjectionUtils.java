package com.rnmap_wb.activity.mapwork;

import android.graphics.Point;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class ProjectionUtils {

    public static GeoPoint  fromScreenPixel(MapView mapView,int x, int y)
    {
        return (GeoPoint) mapView.getProjection().fromPixels(x  , y);
    }

    public static void  toScreenPixel(MapView mapView, GeoPoint geoPoint, Point point)
    {
           mapView.getProjection().toPixels(geoPoint,point);
    }
}
