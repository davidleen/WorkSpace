package com.rnmap_wb.activity.mapwork;

import com.giants3.android.mvp.Presenter;

import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.entity.MapElement;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public interface MapWorkPresenter  extends Presenter<MapWorkViewer> {




    void addNewMapElement(MapElement mapElement);


    /**
     * 下载地图
     * @param  latLngs
     */
     void downloadMap(List<GeoPoint> latLngs,  int fromZoom
     ,int toZoom);

    void addNewPolylinePoint(GeoPoint latLng);
    void addNewPolyline(GeoPoint start,GeoPoint end);

    void addNewCircle(GeoPoint latLng, double radia);

    void addNewRectangle(List<GeoPoint> latLngs);


    void clearMapElements();

    void removeElement(MapElement mapElement);

    void prepare(Task task);

    void onMapPrepare();

    void closePaint();

    void updateMapElement(MapElement o);

    void requestFeekBack(String pointString);

    void addMappingLine(GeoPoint p);


    void addMappingRadius(GeoPoint p);

    void addTracking(List<GeoPoint> points);


    void clearMappingElements();
}
