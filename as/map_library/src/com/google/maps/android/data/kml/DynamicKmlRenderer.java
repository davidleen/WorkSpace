package com.google.maps.android.data.kml;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v4.util.Pools;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.data.DataPolygon;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.LineString;
import com.google.maps.android.data.MultiGeometry;
import com.google.maps.android.data.Point;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DynamicKmlRenderer extends KmlRenderer {

    public static final String TAG="DynamicKmlRenderer";
    CameraPosition cameraPosition = null;
    //所有层级的位置缓存。
    SparseArray<List<LatLng>> visibleLatLngOnZoom = new SparseArray<>();
    //当前层级的位置缓存。
List<LatLng> currentVisibleLatLngs = new ArrayList<>();

    List<LatLng> allLatlngs = new ArrayList<>();


    Pools.SimplePool<Marker> markerSimplePool = new Pools.SimplePool<>(1000);
    Pools.SimplePool<Polyline> polylineSimplePool = new Pools.SimplePool<>(5000);

    List<Marker> visibleMarkers = new ArrayList<>();
    List<Polyline> visiblePolylines = new ArrayList<>();
    public float density=0;
    DynamicKmlRenderer(GoogleMap map, Context context) {
        super(map, context);
        density=context.getResources().getDisplayMetrics().density;
    }


    @Override
    void storeKmlData(HashMap<String, KmlStyle> styles, HashMap<String, String> styleMaps, HashMap<KmlPlacemark, Object> features, ArrayList<KmlContainer> folders, HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays) {


        Set<KmlGroundOverlay> kmlGroundOverlays = groundOverlays.keySet();
        for (KmlGroundOverlay groundOverlay : kmlGroundOverlays) {
            LatLngBounds latLngBox = groundOverlay.getLatLngBox();
            if (latLngBox != null) {
                if (latLngBox.northeast != null)
                    allLatlngs.add(latLngBox.northeast);
                if (latLngBox.southwest != null)
                    allLatlngs.add(latLngBox.southwest);
            }

        }

        for (Feature feature : features.keySet()) {
            Geometry geometry = feature.getGeometry();
            addAll(geometry, allLatlngs);


        }

        addAll(folders, allLatlngs);


        //遍历计算各层级应展示的点。

        int maxZoomLevel = 22;
        for (int i = 0; i < maxZoomLevel; i++) {


            List<LatLng> latLngs = new ArrayList<>();
            visibleLatLngOnZoom.put(i, latLngs);


        }

        for (int j = 0; j < allLatlngs.size(); j++) {

            LatLng latLng = allLatlngs.get(j);


            for (int i = 0; i < maxZoomLevel; i++) {

                if ( i>11|| j % (Math.pow(maxZoomLevel - i, 2)) == 0) {
                    visibleLatLngOnZoom.get(i).add(latLng);

                }


            }
        }


//        visibleLatLngOnZoom.get(maxZoomLevel-1).addAll(allLatlngs);
//          for (int i = maxZoomLevel-2; i >=3  ; i--) {
//            List<LatLng> latLngs = visibleLatLngOnZoom.get(i);
//            latLngs.addAll(visibleLatLngOnZoom.get(i+1));
//            removeTooNestItem(latLngs,0,20,i);
//
//        }


        super.storeKmlData(styles, styleMaps, features, folders, groundOverlays);
    }

    private void addAll(Iterable<KmlContainer> kmlContainers, List<LatLng> latLngs) {

        for (KmlContainer item : kmlContainers) {

            for (Feature feature : item.getPlacemarks()) {

                addAll(feature.getGeometry(), latLngs);
            }
            addAll(item.getContainers(), latLngs);
        }

    }

    private void addAll(Geometry geometry, List<LatLng> latLngs) {

        if (geometry != null) {

            if (geometry instanceof MultiGeometry) {

                List<Geometry> geometries = ((MultiGeometry) geometry).getGeometryObject();
                for (Geometry item : geometries) {
                    addAll(item, latLngs);
                }

            } else if (geometry instanceof DataPolygon) {
                latLngs.addAll(((DataPolygon) geometry).getOuterBoundaryCoordinates());
//                        allLatlngs.addAll(((DataPolygon) geometry).getInnerBoundaryCoordinates());
            } else if (geometry instanceof LineString) {
                latLngs.addAll(((LineString) geometry).getGeometryObject());
            } else if (geometry instanceof Point) {
                latLngs.add(((Point) geometry).getGeometryObject());
            }


        }

    }

    @Override
    protected Marker addPointToMap(MarkerOptions markerOptions, Point point) {
        if (cameraPosition == null) return null;
        LatLng geometryObject = point.getGeometryObject();

            List<LatLng> latLngs = currentVisibleLatLngs;
            if (latLngs != null) {

                if (latLngs.contains(geometryObject)) {
                    Marker marker = createMarker(markerOptions, point);
                    marker.setVisible(true);
                    return marker;
                }
            }


        return null;
    }

    private Marker createMarker(MarkerOptions markerOptions, Point point) {
        Marker marker = null;
        Marker acquire = markerSimplePool.acquire();
        if (acquire != null) {
            marker = acquire;
            marker.setVisible(true);
            marker.setPosition(point.getGeometryObject());
            marker.setTitle(markerOptions.getTitle());
            marker.setSnippet(markerOptions.getSnippet());
            marker.setAlpha(markerOptions.getAlpha());
            marker.setAnchor(markerOptions.getAnchorU(),markerOptions.getAnchorV());

        } else {
            marker = super.addPointToMap(markerOptions, point);

        }
        visibleMarkers.add(marker);
        return marker;
    }


    @Override
    protected Polyline addLineStringToMap(PolylineOptions polylineOptions, LineString lineString) {
        // 判断点是否在屏幕内
        //  if(polylineOptions.getPoints())
        if (cameraPosition == null) return null;

        // 判断点是否在屏幕内
        List<LatLng> latLngs = currentVisibleLatLngs;
        if (latLngs == null) return null;
        List<LatLng> willDrawLatLng = lineString.getGeometryObject();
        for (LatLng latLng : willDrawLatLng) {
            if (latLngs.contains(latLng)) {
                Polyline polyline = createPolyline(polylineOptions, lineString);
                return polyline;
            }
        }


        return null;
    }

    private Polyline createPolyline(PolylineOptions polylineOptions, LineString lineString) {
        Polyline polyline = null;
        Polyline acquire = polylineSimplePool.acquire();
        if (acquire != null) {
            polyline = acquire;
            polyline.setColor(polylineOptions.getColor());
            polyline.setPoints(lineString.getGeometryObject());
            polyline.setVisible(true);
            polyline.setWidth(polylineOptions.getWidth());
        } else {
            polyline = super.addLineStringToMap(polylineOptions, lineString);

        }
        visiblePolylines.add(polyline);
        return polyline;
    }

    @Override
    protected Polygon addPolygonToMap(PolygonOptions polygonOptions, DataPolygon polygon) {


        // 判断点是否在屏幕内
        List<LatLng> latLngs = currentVisibleLatLngs;
        List<LatLng> willDrawLatLng = polygonOptions.getPoints();
        for (LatLng latLng : willDrawLatLng) {
            if (latLngs.contains(latLng)) {
                return super.addPolygonToMap(polygonOptions, polygon);
            }
        }
        return null;
    }

    /**
     * 地图滚动时候处理。
     */
    public void onCameraUpdate() {

        CameraPosition cameraPosition = getMap().getCameraPosition();
        if (cameraPosition == null) return;
        int zoom = (int) cameraPosition.zoom;
        this.cameraPosition = cameraPosition;



           new UpdateFeatureTask().execute();







    }

    private Runnable updateRunnable=new Runnable() {
        @Override
        public void run() {

            invisibleOutBoundItems();
            updateFeatures();
        }
    };


    List<Marker> tempMarkerList = new ArrayList<>();
    List<Polyline> tempPolylineList = new ArrayList<>();

    private void invisibleOutBoundItems() {
        long time=Calendar.getInstance().getTimeInMillis();



        {
        tempMarkerList.clear();
        for (Marker marker : visibleMarkers) {



            if (shouldInScreen(getMap().getProjection(),marker.getPosition())) {


                tempMarkerList.add(marker);

            }else
            {
                //marker.setVisible(false);

                markerSimplePool.release(marker);
            }

        }
            visibleMarkers.clear();
        visibleMarkers.addAll(tempMarkerList);

    }


        {

            tempPolylineList.clear();
            for (Polyline polyline : visiblePolylines) {
                if (shouldInScreen(getMap().getProjection(),polyline.getPoints())) {


                    tempPolylineList.add(polyline);

                }else
                {
                   // polyline.setVisible(false);
                    polylineSimplePool.release(polyline);
                }

            }
            visiblePolylines.clear();
            visiblePolylines.addAll(tempPolylineList);


        }


        Log.e(TAG,"time use invisible item:"+(Calendar.getInstance().getTimeInMillis()-time));
    }


    @Override
    protected HashMap<? extends Feature, Object> getAllFeatures() {
        return super.getAllFeatures();
    }

    /**
     * 根据层级，进行点数过滤
     */
    class UpdateFeatureTask extends AsyncTask<Void,Object,List<LatLng>> {


        LatLng leftTop;
        LatLng rightBottom;

        Projection projection;

        @Override
        protected void onPreExecute() {

            leftTop=getMap().getProjection().fromScreenLocation(new android.graphics.Point(0,0));
            rightBottom=getMap().getProjection().fromScreenLocation(new android.graphics.Point(1080,1920));

            projection=getMap().getProjection();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Object... values) {

            for (Object o:values)
            {
                if(o instanceof  Marker)
                {
                    ((Marker) o).setVisible(false);
                }else
                if(o instanceof Polyline)
                {
                    ((Polyline) o).setVisible(false);
                }
            }
        }

        @Override
        protected List<LatLng> doInBackground(Void[] objects) {






            int zoom = (int) cameraPosition.zoom;



            List<LatLng> visibleItems=new ArrayList<>();
            List<LatLng> latLngs = visibleLatLngOnZoom.get(zoom);
            for (LatLng temp:latLngs)
            {

                if(temp.latitude<=leftTop.latitude
                        &&  temp.latitude>=rightBottom.latitude
                        &&temp.longitude>=leftTop.longitude
                       &&temp.longitude<=rightBottom.longitude
                        )
                {

                    visibleItems.add(temp);
                }
            }





        Log.e(TAG,"visible item  size in screen："+visibleItems.size());
            return visibleItems;
        }

        @Override
        protected void onPostExecute(List<LatLng> o) {


            //updateFeatures();
            currentVisibleLatLngs.clear();
            currentVisibleLatLngs.addAll(o);



            updateRunnable.run();
        }
    }


    private boolean shouldInScreen(Projection projection,List<LatLng> latLngs) {

        for (LatLng latLng : latLngs) {


            if (shouldInScreen(  projection,latLng)) return true;
        }
        return false;

    }

    /**
     * 判断位置是否显示在屏幕中
     *
     * @param latLng
     * @return
     */
    RectF rect=new RectF(0,0,1080,1920);
    private boolean shouldInScreen(Projection projection,LatLng latLng) {


        android.graphics.Point point = projection.toScreenLocation(latLng);
       return  rect.contains(point.x,point.y) ;


    }

    List<LatLng> tempList = new ArrayList<>();

    /**
     * 这个算法递归层级太深。
     *
     * @param latLngs
     * @param startIndex
     * @param minPixel
     * @param zoom
     */
    private void removeTooNestItem(List<LatLng> latLngs, int startIndex, int minPixel, int zoom) {

        if (zoom > 15) return;
        int size = latLngs.size();
        if (startIndex >= size - 1) return;
        LatLng first = latLngs.get(startIndex);

        tempList.clear();
        for (int i = startIndex + 1; i < size; i++) {
            LatLng item = latLngs.get(i);
            if (LatLngPixel.distanceOnPixel(first, item, zoom,density) < minPixel) {
                tempList.add(item);
            }
        }
        if (tempList.size() > 0) {
            latLngs.removeAll(tempList);

        }
        tempList.clear();
        removeTooNestItem(latLngs, startIndex + 1, minPixel, zoom);


    }

    protected void removeFeatures(HashMap<Feature, Object> features) {

    }

}
