package com.rnmap_wb.activity.mapwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.giants3.android.frame.util.Log;
import com.giants3.android.reader.domain.GsonUtils;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.AddMarkActivity;
import com.rnmap_wb.activity.BaseMvpActivity;
import com.rnmap_wb.activity.home.HomeActivity;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.entity.MapElement;
import com.rnmap_wb.immersive.SmartBarUtils;
import com.rnmap_wb.service.DownloadManagerService;
import com.rnmap_wb.utils.IntentConst;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class MapWorkActivity extends BaseMvpActivity<MapWorkPresenter> implements View.OnClickListener, MapWorkViewer {


    private static final int REQUEST_ADD_MARK = 88;
    private static final int REQUEST_UPDATE_MARK = 89;
    @Bind(R.id.topbar)
    View topbar;
    @Bind(R.id.editGroup)
    View editGroup;
    @Bind(R.id.mylocation)
    View mylocation;
    @Bind(R.id.clear)
    View clear;
    @Bind(R.id.toHome)
    View toHome;
    @Bind(R.id.addMark)
    View addMark;
    @Bind(R.id.addCircle)
    View addCircle;
    @Bind(R.id.addRect)
    View addRect;
    @Bind(R.id.edit)
    View edit;
    @Bind(R.id.addPolygon)
    View addPolygon;
    @Bind(R.id.close)
    View close;
    @Bind(R.id.addPolyLine)
    View addPolyLine;
    @Bind(R.id.download)
    View download;
    @Bind(R.id.viewDownLoad)
    View viewDownLoad;
    @Bind(R.id.switchMap)
    View switchMap;
    //    private ClusterManager<GeoObjectItem> mClusterManager;
//    private TileOverlay tileOverlay;
    String kmlFilePath;
    Task task;

    @Bind(R.id.online)
    View btnOnline;
    @Bind(R.id.offline)
    View btnOffLine;


    @Bind(R.id.map)
    MapView mapView;

    private MyLocationHelper myLocationHelper;

    private final static int STATE_ADD_MARK = 1;
    private final static int STATE_ADD_POLYLINE = 2;
    private final static int STATE_ADD_CIRCLE = 3;

    private static final int STATE_ADD_RECTANGLE = 4;

    private final static int STATE_ADD_POLYGON = 5;

    Map<MapElement, Object> elementObjectMap = new HashMap<>();
    Map<Object, MapElement> objectMapElementHashMap = new HashMap<>();

    private int state = 0;
    private PopupWindow mPopupWindow;

    private OverlayWithIWClickListener clickListener;
    private ZoomControllerHelper zoomController;

    public static void start(Activity activity, Task task, String kmlFilePath, int requestCode) {

        Intent intent = new Intent(activity, MapWorkActivity.class);
        intent.putExtra(IntentConst.KEY_TASK_DETAIL, GsonUtils.toJson(task));
        intent.putExtra(IntentConst.PARAM_KML_PATH, kmlFilePath);
        activity.startActivityForResult(intent, requestCode);

    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activiity_main;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kmlFilePath = getIntent().getStringExtra(IntentConst.PARAM_KML_PATH);
        task = GsonUtils.fromJson(getIntent().getStringExtra(IntentConst.KEY_TASK_DETAIL), Task.class);
        if (task == null && BuildConfig.DEBUG) {
            task = new Task();
            task.id = "xxxxxxxxx";
            task.name = "task_name_fffff";
            task.dir_name = "task_dir_name_fffff";
        }
        getPresenter().prepare(task);
        myLocationHelper = new MyLocationHelper(this, mapView);
        zoomController = new ZoomControllerHelper(this, mapView);

        addMark.setOnClickListener(this);
        edit.setOnClickListener(this);
        close.setOnClickListener(this);
        addCircle.setOnClickListener(this);
        addPolygon.setOnClickListener(this);
        addPolyLine.setOnClickListener(this);
        addRect.setOnClickListener(this);
        download.setOnClickListener(this);
        switchMap.setOnClickListener(this);
        viewDownLoad.setOnClickListener(this);
        toHome.setOnClickListener(this);
        btnOffLine.setOnClickListener(this);
        btnOnline.setOnClickListener(this);
        clear.setOnClickListener(this);
        mylocation.setOnClickListener(this);
        clickListener = new OverlayWithIWClickListener();

        getNavigationController().setVisible(false);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topbar.getLayoutParams();
        layoutParams.topMargin = SmartBarUtils.getStatusBarHeight(this);
        topbar.setLayoutParams(layoutParams);

        onMapPrepare();
        zoomController.setZoomAvailable(true);
        myLocationHelper.addMyLocation();
    }


    @Override
    protected MapWorkPresenter createPresenter() {

        return new MapWorkPresenterImpl();
    }

    private class OverlayWithIWClickListener implements Polyline.OnClickListener, Marker.OnMarkerClickListener, Polygon.OnClickListener {

        @Override
        public boolean onMarkerClick(Marker marker, MapView mapView) {


//
            Log.e("marker info windwo click");
            MapElement mapElement = objectMapElementHashMap.get(marker);

            AddMarkActivity.start(MapWorkActivity.this, mapElement, REQUEST_UPDATE_MARK);


            return true;
        }

        @Override
        public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {

//            String[] menu = new String[]{"删除图形", "下载离线地图"};
//            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("操作选择").setItems(menu, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    switch (which) {
//                        case 0:
//                            getPresenter().removeElement(objectMapElementHashMap.get(polygon));
//                            polygon.remove();
//                            break;
//                        case 1:
//                            final List<LatLng> points = polygon.getPoints();
//                            List<LatLng> list = new ArrayList<>();
//                            list.add(points.get(0));
//                            list.add(points.get(2));
//
//                            float maxZoomLevel = Math.min(getMap().getCameraPosition().zoom + 5, getMap().getMaxZoomLevel());
//                            float minZoomLevel = Math.max(getMap().getCameraPosition().zoom - 5, getMap().getMinZoomLevel());
//
//                            pickZoom(list, maxZoomLevel, minZoomLevel);
//                            break;
//                    }
//
//
//                }
//            }).create();
//            alertDialog.show();

            return false;
        }

        @Override
        public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
            doPolylineClick(polyline, mapView, eventPos);
            return true;
        }
    }


    protected void onMapPrepare() {


        getPresenter().onMapPrepare();
        setOnlineState();


        MapEventsOverlay eventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                switch (state) {
                    case STATE_ADD_MARK:
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng).title("Adelaide")
//                                .snippet("Population: 1,213,000");
//                        Marker marker = getMap().addMarker(markerOptions);
//
//                        marker.setVisible(true);
                        Intent intent = new Intent(MapWorkActivity.this, AddMarkActivity.class);
                        intent.putExtra(IntentConst.KEY_LATLNG, (Parcelable) p);
                        startActivityForResult(intent, REQUEST_ADD_MARK);
                        break;
                    case STATE_ADD_POLYLINE:
                        getPresenter().addNewPolylinePoint(p);
                        break;
                    case STATE_ADD_POLYGON:

                    {
//                        int[] screenWH = Utils.getScreenWH();
//                        LatLng right = getMap().getProjection().fromScreenLocation(new Point(screenWH[0] * 2 / 3, screenWH[1] / 2));
//                        LatLng center = getMap().getProjection().fromScreenLocation(new Point(screenWH[0] / 2, screenWH[1] / 2));
//                        List<LatLng> list = new ArrayList<>();
//                        list.add(right);
//
//                        double length = right.longitude - center.longitude;
//                        for (int i = 0; i < 6; i++) {
//
//
//                            double angle = Math.toRadians(i * 60);
//                            double lat = Math.sin(angle) * length + center.latitude;
//                            double lng = Math.cos(angle) * length + center.longitude;
//                            LatLng item = new LatLng(lat, lng);
//                            list.add(item);
//
//                        }
//
//
//                        getPresenter().addNewRectangle(list);
                    }
                    break;


                    case STATE_ADD_RECTANGLE: {
//                        int[] screenWH = Utils.getScreenWH();
//                        LatLng left = getMap().getProjection().fromScreenLocation(new Point(screenWH[0] / 3, screenWH[1] / 3));
//                        LatLng center = getMap().getProjection().fromScreenLocation(new Point(screenWH[0] / 2, screenWH[1] / 2));
//
//                        List<LatLng> latLngs = createRectangle(latLng, center.latitude - left.latitude, center.longitude - left.longitude);
//
//
//                        getPresenter().addNewRectangle(latLngs);
                    }
                    break;

                    case STATE_ADD_CIRCLE: {
//                        int[] screenWH = Utils.getScreenWH();
//                        GeoPoint out = getMap().getProjection().fromScreenLocation(new Point(screenWH[0] / 4, screenWH[1] / 4));
//                        GeoPoint first = getMap().getProjection().fromScreenLocation(new Point(0, 0));
//                        double distance = LatLngPixel.distanceInMeter(first.latitude, first.longitude, out.latitude, out.longitude);
//
//                        getPresenter().addNewCircle(latLng, distance);
                        break;
                    }

                }


                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {


                switch (state) {

                    case STATE_ADD_CIRCLE:


                        return true;


                }
                return false;
            }
        });
        mapView.getOverlays().add(eventsOverlay);


////your items
//        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
//        //items.add(new OverlayItem("Title", "Description", new GeoPoint(0.0d,0.0d))); // Lat/Lon decimal degrees
//
////the overlay
//        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this,items,
//                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//                    @Override
//                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
//
//
//
//
//                        if(item instanceof Marker) {
//
//                            Log.e("marker info windwo click");
//                            MapElement mapElement = objectMapElementHashMap.get(item);
//
//                            AddMarkActivity.start(MapWorkActivity.this, mapElement, REQUEST_UPDATE_MARK);
//                        }
//
//                        return true;
//                    }
//                    @Override
//                    public boolean onItemLongPress(final int index, final OverlayItem item) {
//                        return false;
//                    }
//                });
//        mOverlay.setFocusItemsOnTap(true);

        //   mapView.getOverlays().add(mOverlay);


//        getMap().setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
//            @Override
//            public void onCircleClick(final Circle circle) {
//                String[] menu = new String[]{"删除图形"};
//                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("操作选择").setItems(menu, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        switch (which) {
//                            case 0:
//                                getPresenter().removeElement(objectMapElementHashMap.get(circle));
//                                circle.remove();
//                                break;
//                            case 1:
////                                final List<LatLng> points = polygon.getPoints();
////                                List<LatLng> list = new ArrayList<>();
////                                list.add(points.get(0));
////                                list.add(points.get(2));
////
////                                float maxZoomLevel = Math.min(getMap().getCameraPosition().zoom + 5, getMap().getMaxZoomLevel());
////                                float minZoomLevel = Math.max(getMap().getCameraPosition().zoom - 5, getMap().getMinZoomLevel());
////
////                                pickZoom(list, maxZoomLevel, minZoomLevel);
//                                break;
//                        }
//
//
//                    }
//                }).create();
//                alertDialog.show();
//
//
//            }
//        });


//        new AsyncTask<Void, Void, List<GeoObjectItem>>() {
//            @Override
//            protected List<GeoObjectItem> doInBackground(Void[] objects) {
//
//                return kmlhandle();
//
//            }
//
//            @Override
//            protected void onPostExecute(List<GeoObjectItem> o) {
//                super.onPostExecute(o);
//                if (o == null) return;
//                CustomClusterManager clusterManager = new CustomClusterManager(getMap());
//                clusterManager.addItems(o);
//
//
//                getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(o.get(0).getPosition(), 5));
//
//
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private void doPolylineClick(final Polyline polyline, MapView mapView, IGeoPoint iGeoPoint) {
        String[] menu = new String[]{"删除线条"};
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("操作选择").setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        getPresenter().removeElement(objectMapElementHashMap.get(polyline));
                        polyline.setVisible(false);
                        break;
                    case 1:
//                                final List<LatLng> points = polygon.getPoints();
//                                List<LatLng> list = new ArrayList<>();
//                                list.add(points.get(0));
//                                list.add(points.get(2));
//
//                                float maxZoomLevel = Math.min(getMap().getCameraPosition().zoom + 5, getMap().getMaxZoomLevel());
//                                float minZoomLevel = Math.max(getMap().getCameraPosition().zoom - 5, getMap().getMinZoomLevel());
//
//                                pickZoom(list, maxZoomLevel, minZoomLevel);
                        break;
                }


            }
        }).create();
        alertDialog.show();
    }

    /**
     * 弹窗选择下载缓存区间
     *
     * @param latLngs
     */
    private void pickZoom(final List<GeoPoint> latLngs, float maxZoom, float minzoom) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("选择地图级别");
        final String[] s = new String[(int) maxZoom - (int) minzoom];
        for (int i = 0; i < s.length; i++) {

            s[i] = String.valueOf((int) (minzoom + i));
        }
        builder.setItems(s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(which);

                Integer fromZoom = Integer.valueOf(s[which]);
                getPresenter().downloadMap(latLngs, fromZoom - 1, fromZoom + 1);


            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

//    private List<GeoObjectItem> kmlhandle() {
//        if (!StringUtil.isEmpty(kmlFilePath)) {
//
//
//            InputStream inputStream = null;
//            try {
//                inputStream = new FileInputStream(kmlFilePath);
////                inputStream = getContext().getResources().openRawResource(R.raw.k00002);
//
//                List<Geometry> addGeometry = KmlHelper.getAllPlacemark(inputStream);
//
//                List<GeoObjectItem> geoObjectItems = new ArrayList<>();
//                for (Geometry geometry : addGeometry) {
//
//                    GeoObjectItem geoObjectItem = new GeoObjectItem(geometry, String.valueOf(geometry.hashCode()), "");
//                    geoObjectItems.add(geoObjectItem);
//
//                }
//
//
//                return geoObjectItems;
////
////                createClusterManager(geoObjectItems);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                FileUtils.safeClose(inputStream);
//            }
//
//        }
//        return null;
//    }


    private boolean onLine = true;

    @Override
    public void onClick(View v) {

//
//        int x = getResources().getDisplayMetrics().widthPixels / 2;
//        int y = getResources().getDisplayMetrics().heightPixels / 2;
//
//
//        GeoPoint latLng = getMap().getProjection().fromScreenLocation(new Point(x, y));
//        GeoPoint left = getMap().getProjection().fromScreenLocation(new Point(x / 3, y));
//        GeoPoint leftTop = getMap().getProjection().fromScreenLocation(new Point(x / 3, y / 3));
        switch (v.getId()) {
//
//            case R.id.addCircle:
//
//
//                state = STATE_ADD_CIRCLE;
//
//
//                //   addCircleToMap(getMap(), latLng);
//
//
//                break;
//            case R.id.addPolygon:
//                state = STATE_ADD_POLYGON;
////                addPolygonToMap(getMap(), latLng);
//                break;
            case R.id.close:
                state = 0;
                if (tempPolyline != null) mapView.getOverlays().remove(tempPolyline);
                getPresenter().closePaint();
                editGroup.setVisibility(View.GONE);
                break;
            case R.id.addPolyLine:

                state = STATE_ADD_POLYLINE;

                break;
//            case R.id.addRect:
//
//                state = STATE_ADD_RECTANGLE;
//                break;
            case R.id.edit:

                editGroup.setVisibility(View.VISIBLE);
                break;
//            case R.id.switchMap:
//
//                if (onLine) {
//                    getMap().setMapType(GoogleMap.MAP_TYPE_NONE);
//                    tileOverlay = getMap().addTileOverlay(new TileOverlayOptions()
//                            .tileProvider(new OfflineTileOverlay())
//                            .transparency(0.5f));
//
//                } else {
//                    getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                    if (tileOverlay != null) {
//                        tileOverlay.remove();
//                        tileOverlay = null;
//                    }
//                }
//
//                onLine = !onLine;
//
//                break;
//
//            case R.id.download:
//
//                List<LatLng> latLngs = new ArrayList<>();
//                latLngs.add(leftTop);
//                latLngs.add(latLng);
//                getPresenter().downloadMap(latLngs, 10, 11);
//                break;
//            case R.id.viewDownLoad:
//
//
//                Intent intent = new Intent(this, DownloadTaskListActivity.class);
//                startActivity(intent);
//
//                break;
//
//
            case R.id.addMark:


                state = STATE_ADD_MARK;
                break;
            case R.id.toHome:


                HomeActivity.start(MapWorkActivity.this);
                break;
//
//            case R.id.offline:
//
//                getMap().setMapType(GoogleMap.MAP_TYPE_NONE);
//                tileOverlay = getMap().addTileOverlay(new TileOverlayOptions()
//                        .tileProvider(new OfflineTileOverlay())
//                        .transparency(0.5f));
//
//
//                onLine = false;
//                btnOnline.setSelected(false);
//                btnOffLine.setSelected(true);
//                break;
//            case R.id.online:
//                setOnlineState();
//
//                break;
//            case R.id.mylocation:
//
//                tryLocation();
//                break;
//            case R.id.clear:
//
//                AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("确定移除当前所有标记？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (tempCircle != null)
//                            tempCircle.remove();
//                        if (tempRectangle != null)
//                            tempRectangle.remove();
//                        if (tempPolyline != null)
//                            tempPolyline.remove();
//                        Set<Object> objects = objectMapElementHashMap.keySet();
//                        for (Object o : objects) {
//                            if (o instanceof Marker) {
//
//                                ((Marker) o).remove();
//                            } else if (o instanceof Polyline) {
//
//                                ((Polyline) o).remove();
//                            } else if (o instanceof Polygon) {
//
//                                ((Polygon) o).remove();
//                            } else if (o instanceof Circle) {
//
//                                ((Circle) o).remove();
//                            }
//                        }
//
//                        objectMapElementHashMap.clear();
//                        elementObjectMap.clear();
//                        getPresenter().clearMapElements();
//                    }
//                }).create();
//                alertDialog.show();
//
//                break;
        }

    }


    /**
     * 尝试定位当前位置
     */
    private void tryLocation() {


//        LocationManager service = (LocationManager)
//
//                getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = service.getBestProvider(criteria, false);
//        Location location = service.getLastKnownLocation(provider);
//        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        getMap().animateCamera(CameraUpdateFactory.newLatLng(userLocation));
    }

    private void setOnlineState() {
//        getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        if (tileOverlay != null) {
//            tileOverlay.remove();
//            tileOverlay = null;
//        }
//        btnOnline.setSelected(true);
//        btnOffLine.setSelected(false);
//        onLine = true;
    }

    private void addCircleToMap(MapView mapView, GeoPoint latLng) {


//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(latLng).radius(1000000).fillColor(Color.parseColor("#FF0000")).strokeWidth(10).strokeColor(Color.parseColor("#FFff00"));
//        Circle circle = googleMap.addCircle(circleOptions);
//        circle.setVisible(true);
    }

    private void addPolyLineToMap(MapView mapView, List<GeoPoint> latLng) {


        // A geodesic polyline that goes around the world.
        Polyline polyline = new Polyline();
        polyline.setPoints(latLng);
        polyline.setOnClickListener(clickListener);
        polyline.setVisible(true)
        ;
        polyline.setWidth(10);
        polyline.setColor(Color.BLUE);
        mapView.getOverlays().add(polyline);
        polyline.setVisible(true);

    }

    private void addPolygonToMap(MapView mapView, List<GeoPoint>... latLng) {

//
//        // A geodesic polyline that goes around the world.
//        googleMap.addPolygon(new PolygonOptions()
//                .addAll(createRectangle(latLng[0], 5, 5))
//                .addHole(createRectangle(new LatLng(-22, 128), 1, 1))
//                .addHole(createRectangle(new LatLng(-18, 133), 0.5, 1.5))
////            .fillColor(fillColorArgb)
//                .strokeColor(Color.argb(255, 255, 0, 0))
//                .strokeWidth(11)
//                .clickable(true));
    }


//    /**
//     * Creates a List of LatLngs that form a rectangle with the given dimensions.
//     */
//    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
//        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
//                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
//                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
//                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
//                new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
//    }


    Polyline tempPolyline = null;

    @Override
    public void showPolyLine(List<GeoPoint> polyLinePositions) {


        if (tempPolyline == null) {
            tempPolyline = new Polyline();
            mapView.getOverlays().add(tempPolyline);
        }
        tempPolyline.setPoints(polyLinePositions);
        mapView.invalidate();


    }


    @Override
    public void showCircle(GeoPoint center, double radio) {


//        if (tempCircle != null) tempCircle.remove();
//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.radius(radio);
//        circleOptions.center(center);
//        circleOptions.clickable(true);
//
//
//        tempCircle = getMap().addCircle(circleOptions);


    }

    private Polygon tempRectangle;

    @Override
    public void showRectangle(List<GeoPoint> rectangle) {
//        if (tempRectangle != null) tempRectangle.remove();
//        PolygonOptions polygonOptions = new PolygonOptions();
//        polygonOptions.addAll(rectangle);
//        polygonOptions.clickable(true);
//
//
//        tempRectangle = getMap().addPolygon(polygonOptions);
    }

    @Override
    public void startDownLoadTask(Long taskId) {

        Intent serviceIntent = new Intent(MapWorkActivity.this, DownloadManagerService.class);
        serviceIntent.putExtra(IntentConst.KEY_TASK_ID, taskId);
        startService(serviceIntent);


    }


    @Override
    public void removeMapElement(MapElement replaced) {
        Object remove = elementObjectMap.remove(replaced);
        objectMapElementHashMap.remove(remove);
        if (remove instanceof OverlayWithIW) {
            mapView.getOverlays().remove(remove);
        }
    }

    @Override
    public void showMapElement(MapElement element) {

        List<GeoPoint> latLngs = LatLngUtil.convertStringToGeoPoints(element.latLngs);

        Object o = null;
        switch (element.type) {
            case MapElement.TYPE_MARKER: {

                GeoPoint geoPoint = latLngs.get(0);
                Marker marker = new Marker(mapView);
                marker.setTitle(element.name);
                marker.setPosition(geoPoint);
                marker.setSnippet(element.memo);
                marker.setOnMarkerClickListener(clickListener);
                mapView.getOverlays().add(marker);
                mapView.invalidate();
                o = marker;


//                LatLng latLng = latLngs.get(0);
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng).title(element.name).snippet(element.memo);
//                Marker marker = getMap().addMarker(markerOptions);
//                marker.setVisible(true);
//                o = marker;

            }
            break;
            case MapElement.TYPE_POLYLINE:

//                // A geodesic polyline that goes around the world.
//                o = getMap().addPolyline(new PolylineOptions().addAll(latLngs)
//                        .width(10)
//                        .color(Color.BLUE)
//                        .geodesic(true)
//                        .clickable(true));
                Polyline polyline = new Polyline(mapView);
                polyline.setTitle(element.name);
                polyline.setPoints(latLngs);
                polyline.setSnippet(element.memo);
                polyline.setOnClickListener(clickListener);
                mapView.getOverlays().add(polyline);
                mapView.invalidate();
                o = polyline;


                break;
            case MapElement.TYPE_POLYGON:

//                PolygonOptions polygonOptions = new PolygonOptions();
//                polygonOptions.addAll(latLngs);
//                polygonOptions.clickable(true);
//                o = getMap().addPolygon(polygonOptions);


                break;
            case MapElement.TYPE_CIRCLE:

//                CircleOptions circleOptions = new CircleOptions();
//                circleOptions.radius(element.radius);
//                circleOptions.center(latLngs.get(0));
//                circleOptions.clickable(true);
//
//
//                o = getMap().addCircle(circleOptions);
//

                break;

        }
        elementObjectMap.put(element, o);
        objectMapElementHashMap.put(o, element);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_ADD_MARK: {
                MapElement o = GsonUtils.fromJson(data.getStringExtra(IntentConst.KEY_MAP_ELEMENT), MapElement.class);
                getPresenter().addNewMapElement(o);

            }
            break;
            case REQUEST_UPDATE_MARK: {
                MapElement o = GsonUtils.fromJson(data.getStringExtra(IntentConst.KEY_MAP_ELEMENT), MapElement.class);

                getPresenter().updateMapElement(o);

            }
            break;
        }
    }


    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


}
