package com.rnmap_wb.activity.mapwork;

import com.google.maps.android.clustering.ClusterItem;
import com.rnmap_wb.LatLngUtil;

import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.util.List;

public class GeoObjectItem implements ClusterItem {

    public KmlGeometry mGeometry;
    private GeoPoint mPosition;
    private String mTitle;
    private String mSnippet;

    public GeoObjectItem(double lat, double lng) {
        mPosition = new GeoPoint(lat, lng);
        mTitle = null;
        mSnippet = null;
    }

    public GeoObjectItem(GeoPoint latLng, String title, String snippet) {
        mPosition = latLng;
        mTitle = title;
        mSnippet = snippet;
    }

    private GeoPoint findCenter(List<GeoPoint> latLngs) {


        double lat = 0;
        double lng = 0;


        for (GeoPoint latLng : latLngs) {

            lat += latLng.getLatitude();
            lng += latLng.getLongitude();

        }

        int size = latLngs.size();
        GeoPoint center = new GeoPoint(lat / size, lng / size);
        return center;


    }

    public GeoObjectItem(KmlGeometry geometry, String title, String snippet) {


        if (geometry instanceof KmlPolygon) {
            mPosition = findCenter(((KmlPolygon) geometry).mCoordinates);
        } else if (geometry instanceof KmlLineString) {
            List<GeoPoint> geometryObject = ((KmlLineString) geometry).mCoordinates;
            mPosition = findCenter(geometryObject);
        } else if (geometry instanceof KmlPoint) {

            mPosition = (((KmlPoint) geometry).getPosition());
        }

        mGeometry = geometry;
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public GeoPoint getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    /**
     * Set the title of the marker
     *
     * @param title string to be set as title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Set the description of the marker
     *
     * @param snippet string to be set as snippet
     */
    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }

    public boolean isInBounds(BoundingBox bounds) {

        if (bounds.contains(mPosition)) return true;

        if (mGeometry instanceof KmlPolygon) {

            List<GeoPoint> outerBoundaryCoordinates = ((KmlPolygon) mGeometry).mCoordinates;
            if (LatLngUtil.linesIntersectRect(outerBoundaryCoordinates, bounds)) {
                return true;
            }

        } else if (mGeometry instanceof KmlLineString) {
            List<GeoPoint> geometryObject = ((KmlLineString) mGeometry).mCoordinates;
            if (LatLngUtil.linesIntersectRect(geometryObject, bounds)) {
                return true;
            }


        } else if (mGeometry instanceof KmlPoint) {


            if (bounds.contains((((KmlPoint) mGeometry).getPosition()))) return true;
        }


        return false;
    }


}
