package com.rnmap_wb.activity.mapwork;

import com.rnmap_wb.entity.MapElement;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class MapWorkModelImpl implements MapWorkModel {

    List<GeoPoint> polylines = new ArrayList<>();

    List<MapElement> elements = new ArrayList<>();

    @Override
    public void addNewPolylinePoint(GeoPoint latLng) {
        polylines.add(latLng);


    }

    @Override
    public List<GeoPoint> getPolyLinePositions() {
        return polylines;
    }


    private GeoPoint circleCenter;
    private double radius;


    @Override
    public void addNewCircle(GeoPoint latLng, double radius) {


        this.circleCenter = latLng;
        this.radius = radius;
    }

    @Override
    public GeoPoint getCircleCenter() {
        return circleCenter;
    }


    @Override
    public double getRadius() {
        return radius;
    }


    private List<GeoPoint> rectLatLngs;

    @Override
    public void addNewRectangle(List<GeoPoint> latLngs) {
        rectLatLngs = latLngs;
    }

    @Override
    public List<GeoPoint> getRectangle() {
        return rectLatLngs;
    }

    @Override
    public void addNewMapElement(MapElement element) {

        elements.add(element);


    }

    @Override
    public void clearEmelemnts() {
        elements.clear();
    }

    @Override
    public void removeElement(MapElement mapElement) {
        elements.remove(mapElement);
    }

    @Override
    public void setMapElements(List<MapElement> mapElements) {
        elements = mapElements;
    }

    @Override
    public List<MapElement> getMapElements() {
        return elements;
    }

    @Override
    public void clearPolyLinePosition() {


        if (polylines != null)
            polylines.clear();
    }

    @Override
    public MapElement updateMapElement(MapElement o) {


        MapElement find = null;
        for (MapElement mapElement : elements) {
            if (o.uuid.equals(mapElement.uuid)) {
                find
                        = mapElement;
                break;

            }
        }
        if (find != null)
            elements.remove(find);
        elements.add(o);
        return find;
    }
}
