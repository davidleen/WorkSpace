package com.rnmap_wb.activity.mapwork;

import com.giants3.android.mvp.Model;
import com.rnmap_wb.entity.MapElement;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public interface MapWorkModel extends Model {


    void addNewPolylinePoint(GeoPoint latLng);

    List<GeoPoint> getPolyLinePositions();

    void addNewCircle(GeoPoint latLng, double radia);

    GeoPoint getCircleCenter();

    double getRadius();

    void addNewRectangle(List<GeoPoint> latLngs);

    List<GeoPoint> getRectangle();

    void addNewMapElement(MapElement latLng);

    void clearEmelemnts();

    void removeElement(MapElement mapElement);

    void setMapElements(List<MapElement> mapElements);

    List<MapElement> getMapElements();

    void clearPolyLinePosition();

    MapElement updateMapElement(MapElement o);

    MapElement getMapElementByPoint(String pointString);

    void setEditElement(MapElement mapElement);

    MapElement getEdittingMapElement();
}
