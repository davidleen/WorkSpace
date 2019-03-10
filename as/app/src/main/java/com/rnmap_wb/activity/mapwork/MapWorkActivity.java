package com.rnmap_wb.activity.mapwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.Utils;
import com.giants3.android.reader.domain.GsonUtils;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.AddMarkActivity;
import com.rnmap_wb.activity.BaseMvpActivity;
import com.rnmap_wb.activity.FeedBackDialog;
import com.rnmap_wb.activity.home.HomeActivity;
import com.rnmap_wb.activity.mapwork.map.Circle;
import com.rnmap_wb.activity.mapwork.map.CustomMarker;
import com.rnmap_wb.activity.mapwork.map.CustomPolygon;
import com.rnmap_wb.activity.mapwork.map.CustomPolyline;
import com.rnmap_wb.activity.mapwork.map.MappingPolyline;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.entity.MapElement;
import com.rnmap_wb.immersive.SmartBarUtils;
import com.rnmap_wb.map.CustomClusterManager;
import com.rnmap_wb.service.DownloadManagerService;
import com.rnmap_wb.utils.IntentConst;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.events.MapAdapter;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Bind(R.id.feedback)
    View feedback;
    @Bind(R.id.clear)
    View clear;
    @Bind(R.id.back)
    View back;
    @Bind(R.id.addMark)
    View addMark;
    @Bind(R.id.addCircle)
    View addCircle;
    @Bind(R.id.addMapping)
    View addMapping;
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
    @Bind(R.id.task_name)
    TextView task_name;
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
    @Bind(R.id.zoomlevel)
    TextView zoomlevel;

    private MyLocationHelper myLocationHelper;

    private final static int STATE_ADD_MARK = 1;
    private final static int STATE_ADD_POLYLINE = 2;
    private final static int STATE_ADD_CIRCLE = 3;

    private static final int STATE_ADD_RECTANGLE = 4;

    private final static int STATE_ADD_POLYGON = 5;
    private final static int STATE_ADD_MAPPING_LINE = 6;

    Map<MapElement, Object> elementObjectMap = new HashMap<>();
    Map<Object, MapElement> objectMapElementHashMap = new HashMap<>();

    private int state = 0;
    private PopupWindow mPopupWindow;

    private OverlayWithIWClickListener clickListener;
    private ZoomControllerHelper zoomController;


    MarkerInfoWindow markerInfoWindow;
    private MapTileHelper mapTileHelper;

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

        task_name.setText(task.name);
        myLocationHelper = new MyLocationHelper(this, mapView);
        zoomController = new ZoomControllerHelper(this, mapView);

        addMark.setOnClickListener(this);
        edit.setOnClickListener(this);
        close.setOnClickListener(this);
        addCircle.setOnClickListener(this);
        addPolygon.setOnClickListener(this);
        addPolyLine.setOnClickListener(this);
        addMapping.setOnClickListener(this);
        addRect.setOnClickListener(this);
        download.setOnClickListener(this);
        switchMap.setOnClickListener(this);
        viewDownLoad.setOnClickListener(this);
        back.setOnClickListener(this);
        btnOffLine.setOnClickListener(this);
        btnOnline.setOnClickListener(this);
        clear.setOnClickListener(this);
        mylocation.setOnClickListener(this);
        feedback.setOnClickListener(this);
        clickListener = new OverlayWithIWClickListener();

        getNavigationController().setVisible(false);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topbar.getLayoutParams();
        layoutParams.topMargin = SmartBarUtils.getStatusBarHeight(this);
        topbar.setLayoutParams(layoutParams);
        onMapPrepare();
        zoomController.setZoomAvailable(true);
        myLocationHelper.addMyLocation();
        mapView.setMinZoomLevel((double) getResources().getDisplayMetrics().density);
//        mapView.getController().animateTo(new GeoPoint(-23d, 33d, 1d), 5d, 3000l);

        loadKML(kmlFilePath);
        try {
            mapTileHelper = new MapTileHelper(this, mapView);
            mapTileHelper.setOnLineMode(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.addMapListener(new MapAdapter() {
            @Override
            public boolean onZoom(ZoomEvent event) {

                zoomlevel.setText("" + (int) event.getZoomLevel());


                return super.onZoom(event);


            }
        });

        markerInfoWindow = new MarkerInfoWindow(R.layout.layout_info_view, mapView);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                zoomlevel.setText("" + mapView.getZoomLevel());
            }
        });


    }


    @Override
    protected MapWorkPresenter createPresenter() {

        return new MapWorkPresenterImpl();
    }

    private void closeMarkerInfoWindow() {
        try {
            if (markerInfoWindow.isOpen()) {

                markerInfoWindow.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class OverlayWithIWClickListener implements Polyline.OnClickListener, Marker.OnMarkerClickListener, Polygon.OnClickListener {

        @Override
        public boolean onMarkerClick(final Marker marker, MapView mapView) {
            if(!(marker instanceof CustomMarker)) return   false;
            final MapElement mapElement = objectMapElementHashMap.get(marker);
            AddMarkActivity.start(MapWorkActivity.this, mapElement, REQUEST_UPDATE_MARK);


//            if (mapElement != null) {
//                final int markerWidth = marker.getIcon().getIntrinsicWidth();
//                final int markerHeight = marker.getIcon().getIntrinsicHeight();
//                final int offsetX = (int) (markerWidth);//* (marker.mIWAnchorU - marker.mAnchorU));
//                final int offsetY = (int) (markerHeight);//* (marker.mIWAnchorV - marker.mAnchorV));
//
//                markerInfoWindow.open(marker, marker.getPosition(), offsetX * 2, offsetY * 2);
//                markerInfoWindow.getView().findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        markerInfoWindow.close();
//
//                    }
//                });
//                //
//            } else if (CustomClusterManager.CLUSTER.equals(marker.getSnippet())) {
//                marker.showInfoWindow();
//            } else {
//                final int markerWidth = marker.getIcon().getIntrinsicWidth();
//                final int markerHeight = marker.getIcon().getIntrinsicHeight();
//                final int offsetX = (int) (markerWidth);//* (marker.mIWAnchorU - marker.mAnchorU));
//                final int offsetY = (int) (markerHeight);//* (marker.mIWAnchorV - marker.mAnchorV));
//
//                markerInfoWindow.open(marker, marker.getPosition(), offsetX * 2, offsetY * 2);
//                markerInfoWindow.getView().findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        markerInfoWindow.close();
//                        getPresenter().requestFeekBack(LatLngUtil.convertGeoPointToString(marker.getPosition()));
//                    }
//                });
//
//
////                //kml标记文件
////                String[] menu=new String[]{"反馈"};
////                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle(marker.getSnippet()).setItems(menu, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////
////                        switch (which) {
////                            case 0:
////                                      getPresenter().requestFeekBack(LatLngUtil.convertGeoPointToString(marker.getPosition()));
////
////
////                                break;
////
////                        }
////
////
////                    }
////                }).create();
////                alertDialog.show();
//
//
//            }
//
//
////
////            Log.e("marker info windwo click");
//


            return true;
        }

        @Override
        public boolean onClick(final Polygon polygon, final MapView mapView, GeoPoint eventPos) {

            String[] menu = new String[]{"删除图形", "下载离线地图"};
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("操作选择").setItems(menu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case 0:
                            getPresenter().removeElement(objectMapElementHashMap.get(polygon));
                            mapView.getOverlays().remove(polygon);
                            mapView.invalidate();

                            break;
                        case 1:
                            final List<GeoPoint> points = polygon.getPoints();
                            List<GeoPoint> list = new ArrayList<>();
                            list.add(points.get(0));
                            list.add(points.get(2));

                            float maxZoomLevel = (float) Math.min(mapView.getZoomLevelDouble() + 5, mapView.getMaxZoomLevel());
                            float minZoomLevel = (float) Math.max(mapView.getZoomLevelDouble() - 5, mapView.getMinZoomLevel());

                            pickZoom(list, maxZoomLevel, minZoomLevel);
                            break;
                    }


                }
            }).create();
            alertDialog.show();

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


        MapEventsOverlay eventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                switch (state) {
                    case STATE_ADD_MARK:
                        Intent intent = new Intent(MapWorkActivity.this, AddMarkActivity.class);
                        intent.putExtra(IntentConst.KEY_LATLNG, (Parcelable) p);
                        startActivityForResult(intent, REQUEST_ADD_MARK);
                        break;
                    case STATE_ADD_POLYLINE:
                        getPresenter().addNewPolylinePoint(p);
                        break;
                    case STATE_ADD_MAPPING_LINE:


                        getPresenter().addMappingLine(p);
                        break;
                    case STATE_ADD_POLYGON:

                    {
                        Point click = new Point();
                        ProjectionUtils.toScreenPixel(mapView, p, click);
                        int[] screenWH = Utils.getScreenWH();

                        float length = screenWH[0] / 4;

                        Point point = new Point();
                        List<GeoPoint> list = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {


                            double angle = Math.toRadians(i * 60);
                            point.x = (int) (Math.sin(angle) * length + click.x);
                            point.y = (int) (Math.cos(angle) * length + click.y);
                            GeoPoint item = ProjectionUtils.fromScreenPixel(mapView, point.x, point.y);
                            list.add(item);

                        }


                        getPresenter().addNewRectangle(list);
                    }
                    break;


                    case STATE_ADD_RECTANGLE: {
                        int[] screenWH = Utils.getScreenWH();
                        GeoPoint left = ProjectionUtils.fromScreenPixel(mapView, screenWH[0] / 3, screenWH[1] / 3);
                        GeoPoint center = ProjectionUtils.fromScreenPixel(mapView, screenWH[0] / 2, screenWH[1] / 2);

                        List<GeoPoint> latLngs = createRectangle(p, center.getLatitude() - left.getLatitude(), center.getLongitude() - left.getLongitude());


                        getPresenter().addNewRectangle(latLngs);
                    }
                    break;

                    case STATE_ADD_CIRCLE: {
                        int[] screenWH = Utils.getScreenWH();
                        Point point = new Point();
                        ProjectionUtils.toScreenPixel(mapView, p, point);
                        point.x += screenWH[0] / 4;
                        GeoPoint geoPoint = ProjectionUtils.fromScreenPixel(mapView, point.x, point.y);
                        double distanceInMeter = LatLngUtil.distanceInMeter(p , geoPoint );


                        getPresenter().addNewCircle(p, distanceInMeter);
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


//        //比例尺配置
//        final DisplayMetrics dm = getResources().getDisplayMetrics();
//        ScaleBarOverlay  mScaleBarOverlay = new ScaleBarOverlay(mapView);
//
//        mScaleBarOverlay.setAlignRight(true); //右边
//        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels , 80);
//        mapView.getOverlays().add(mScaleBarOverlay);


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

    private void loadKML(final String kmlFilePath) {

        new AsyncTask<Void, Void, KmlDocument>() {

            boolean parserResult;

            @Override
            protected KmlDocument doInBackground(Void... voids) {

                KmlDocument kmlDocument = new KmlDocument();
                boolean b = false;
                long time = Calendar.getInstance().getTimeInMillis();
                try {
                    b = kmlDocument.parseKMLFile(new File(kmlFilePath));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                Log.e("parse result:" + b + ",time:" + (Calendar.getInstance().getTimeInMillis() - time));
//
//                if (!b) {
//                 b = kmlDocument.parseKMLStream(getResources().openRawResource(R.raw.k00002), null);
//                    //b = kmlDocument.parseKMLStream(getResources().openRawResource(R.raw.campus), null);
//                     //b = kmlDocument.parseKMLStream(getResources().openRawResource(R.raw.kmlgeometrytest), null);
//                }


                parserResult = b;


                return kmlDocument;
            }

            @Override
            protected void onPostExecute(KmlDocument kmlDocument) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (isDestroyed()) return;
                }
                if (mapView == null) return;
                if (!parserResult) {
                    ToastHelper.show("KML 文件解析失败");
                    return;
                }

                CustomClusterManager customClusterManager = new CustomClusterManager(mapView, kmlDocument, clickListener);
                mapView.invalidate();

//                FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, null, kmlDocument);
//                mapView.getOverlays().add(kmlOverlay);
//                mapView.invalidate();

                BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
                mapView.getController().animateTo(bb.getCenterWithDateLine(), 10d, 3000l);

            }
        }

                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
        addPolyLine.setSelected(v.getId() == R.id.addPolyLine);
        addMark.setSelected(v.getId() == R.id.addMark);
        addPolygon.setSelected(v.getId() == R.id.addPolygon);
        addCircle.setSelected(v.getId() == R.id.addCircle);
        addMapping.setSelected(v.getId() == R.id.addMapping);

        switch (v.getId()) {

//
            case R.id.addCircle:


                state = STATE_ADD_CIRCLE;


                break;
            case R.id.addPolygon:
                state = STATE_ADD_POLYGON;

                break;
            case R.id.close:
                state = 0;

                editGroup.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                break;
            case R.id.addMapping:

                state = STATE_ADD_MAPPING_LINE;

                break;
            case R.id.addPolyLine:

                state = STATE_ADD_POLYLINE;

                break;
            case R.id.addRect:

                state = STATE_ADD_RECTANGLE;

                break;
            case R.id.edit:
                edit.setVisibility(View.GONE);
                editGroup.setVisibility(View.VISIBLE);
                break;
            case R.id.switchMap:

                if (onLine) {
                    mapTileHelper.setOnLineMode(false);
                    mapView.invalidate();

                } else {
                    setOnlineState();
                }

                onLine = !onLine;
                mapView.setSelected(onLine);

                break;
//
//            case R.id.download:
//
//                List<LatLng> latLngs = new ArrayList<>();
//                latLngs.add(leftTop);
//                latLngs.add(latLng);
//                getPresenter().downloadMap(latLngs, 10, 11);
//                break;
            case R.id.feedback:


                FeedBackDialog.start(this, task);

                break;
//
//
            case R.id.addMark:


                state = STATE_ADD_MARK;
                break;
            case R.id.back:


                finish();

                break;
//
            case R.id.offline:

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

                mapTileHelper.setOnLineMode(false);
                mapView.invalidate();
                break;
            case R.id.online:
                setOnlineState();

                break;
            case R.id.mylocation:

                tryLocation();
                break;
            case R.id.clear:

                AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("确定移除当前所有标记？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        List<Overlay> overlays = mapView.getOverlays();


                        Set<Object> objects = objectMapElementHashMap.keySet();
                        for (Object o : objects) {
                            if (o instanceof OverlayWithIW) {
                                overlays.remove(o);
                            }
                        }

                        objectMapElementHashMap.clear();
                        elementObjectMap.clear();
                        getPresenter().clearMapElements();
                        mapView.invalidate();
                    }
                }).create();
                alertDialog.show();

                break;
        }

    }


    /**
     * 尝试定位当前位置
     */
    private void tryLocation() {


        myLocationHelper.getLocation(this);
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

        mapTileHelper.setOnLineMode(true);
        mapView.invalidate();
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


    /**
     * Creates a List of LatLngs that form a rectangle with the given dimensions.
     */
    private List<GeoPoint> createRectangle(GeoPoint center, double halfWidth, double halfHeight) {
        return Arrays.asList(new GeoPoint(center.getLatitude() - halfHeight, center.getLongitude() - halfWidth),
                new GeoPoint(center.getLatitude() - halfHeight, center.getLongitude() + halfWidth),
                new GeoPoint(center.getLatitude() + halfHeight, center.getLongitude() + halfWidth),
                new GeoPoint(center.getLatitude() + halfHeight, center.getLongitude() - halfWidth),
                new GeoPoint(center.getLatitude() - halfHeight, center.getLongitude() - halfWidth));
    }



    @Override
    public void showPolyLine(List<GeoPoint> polyLinePositions) {




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
                Marker marker = new CustomMarker(mapView);
                marker.setTitle(element.name);
                marker.setPosition(geoPoint);
                marker.setIcon(getResources().getDrawable(R.drawable.icon_map_mark));
                marker.setSnippet(element.memo);
                marker.setOnMarkerClickListener(clickListener);
                ((CustomMarker) marker).bindData(element);
                marker.getInfoWindow().getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Log.e("infoWindow click!!!!!");


                    }
                });
                mapView.getOverlays().add(marker);
                mapView.invalidate();
                o = marker;


            }
            break;
            case MapElement.TYPE_POLYLINE: {

                CustomPolyline polyline = new CustomPolyline();
                polyline.setTitle(element.name);
                polyline.setGeoPoints(latLngs);
                polyline.setSnippet(element.memo);
                //polyline.setOnClickListener(clickListener);
                mapView.getOverlays().add(polyline);
                mapView.invalidate();
                o = polyline;

            }

            break;
            case MapElement.TYPE_MAPPING_LINE: {

                MappingPolyline polyline = new MappingPolyline();
                polyline.setTitle(element.name);
                polyline.setGeoPoints(latLngs);
                polyline.setSnippet(element.memo);
                //polyline.setOnClickListener(clickListener);
                mapView.getOverlays().add(polyline);
                mapView.invalidate();
                o = polyline;

            }

            break;
            case MapElement.TYPE_POLYGON:


                Polygon polygon = new CustomPolygon();
                polygon.setTitle(element.name);
                polygon.setPoints(latLngs);
                polygon.setSnippet(element.memo);
                polygon.setOnClickListener(clickListener);
                mapView.getOverlays().add(polygon);
                mapView.invalidate();
                o = polygon;


                break;
            case MapElement.TYPE_CIRCLE:


                Circle circle = new Circle();


                circle.setRadius(element.radius);
                circle.setCenter(latLngs.get(0));
                circle.setClickable(true);
                mapView.getOverlays().add(circle);
                mapView.invalidate();
                o = circle;


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


    @Override
    public void feedBack(MapElement mapElement) {


        AddMarkActivity.start(MapWorkActivity.this, mapElement, REQUEST_UPDATE_MARK);
    }
}
