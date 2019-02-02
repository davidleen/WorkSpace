package com.rnmap_wb.map;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.giants3.android.frame.util.Log;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;
import com.rnmap_wb.activity.mapwork.GeoObjectItem;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.OverlayWithIW;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomClusterManager implements MapListener {


    KmlHelper kmlHelper;
    public static final int MSG_RENDER = 111;
    public static final String CLUSTER = "放大查看";


    private List<GeoObjectItem> geoObjectItems;

    Handler handler;
    Algorithm mAlgorithm;
    private MapView mapView;
    private KmlDocument kmlDocument;
    ClusterTask mClusterTask;
    private final ReadWriteLock mAlgorithmLock = new ReentrantReadWriteLock();

    private GeoObjectItemGenerator generator;

    FolderOverlay folderOverlay;

    public CustomClusterManager(MapView map, KmlDocument kmlDocument) {
        this.mapView = map;
        this.kmlDocument = kmlDocument;

        mapView.addMapListener(this);
        generator = new GeoObjectItemGenerator();
        mAlgorithm = new PreCachingAlgorithmDecorator<GeoObjectItem>(new NonHierarchicalDistanceBasedAlgorithm<GeoObjectItem>());
        handler = new ModifyViewHandler();
        mClusterTask = new ClusterTask();

        folderOverlay = generator.buildFolderOverlay(map, null, null, kmlDocument);
        mapView.getOverlays().add(folderOverlay);

        kmlHelper = new KmlHelper();
        List<GeoObjectItem> geoObjectItems = new ArrayList<>();
        for (KmlGeometry geometry : kmlHelper.getAllKmlGeometry(kmlDocument)) {

            GeoObjectItem geoObjectItem = new GeoObjectItem(geometry, String.valueOf(geometry.hashCode()), "");
            geoObjectItems.add(geoObjectItem);

        }
        addItems(geoObjectItems);
    }

    @Override
    public boolean onScroll(ScrollEvent event) {


        handler.removeMessages(MSG_RENDER);
        handler.sendEmptyMessageDelayed(MSG_RENDER, 500);
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }

    /**
     * Runs the clustering algorithm in a background thread, then re-paints when results come back.
     */
    private class ClusterTask extends AsyncTask<Object, Void, Set<? extends Cluster<GeoObjectItem>>> {
        @Override
        protected Set<? extends Cluster<GeoObjectItem>> doInBackground(Object... zoom) {
            mAlgorithmLock.readLock().lock();
            try {
                if (isCancelled()) return null;
                mAlgorithm.clearItems();
                BoundingBox bounds = (BoundingBox) zoom[1];
                //过滤不再显示范围内的集合。
                List<GeoObjectItem> items = new ArrayList<>();
                for (GeoObjectItem item : geoObjectItems) {
                    //   if (item.isInBounds(bounds))

                    items.add(item);
                }

                mAlgorithm.addItems(items);
                Set clusters = mAlgorithm.getClusters(Float.valueOf(zoom[0].toString()));

                if (isCancelled()) return null;


                return clusters;
            } finally {
                mAlgorithmLock.readLock().unlock();
            }
        }

        @Override
        protected void onPostExecute(Set<? extends Cluster<GeoObjectItem>> clusters) {

            if (clusters == null) return;

            long time = Calendar.getInstance().getTimeInMillis();
            generator.clearUnVisibleItems(getVisibleLatLngBounds());
            Log.e("tiem use in clear unvisible:" + (Calendar.getInstance().getTimeInMillis() - time));
            Set visilbeCluster = new HashSet<>();
            BoundingBox latLngs = getVisibleLatLngBounds();
            //过滤不再显示范围内的集合。
            for (Cluster<GeoObjectItem> cluster : clusters) {
                if (latLngs.contains(cluster.getPosition()))
                    visilbeCluster.add(cluster);
            }


            addNewMapItem(visilbeCluster);

            Log.e("tiem use in generate:" + (Calendar.getInstance().getTimeInMillis() - time));

            // mRenderer.onClustersChanged(clusters);
        }
    }

    public void addItems(List<GeoObjectItem> geoObjectItems) {

        this.geoObjectItems = geoObjectItems;

    }


    private BoundingBox getVisibleLatLngBounds() {
        BoundingBox boundingBox = mapView.getBoundingBox();

//        double centerLat = boundingBox.getCenterLatitude();
//        double centerLong = boundingBox.getCenterLongitude();
//
//        BoundingBox doubleBox = new BoundingBox(boundingBox.getLatNorth() * 2 - centerLat,
//                boundingBox.getLonEast() * 2 - centerLong,
//                boundingBox.getLatSouth() * 2 - centerLat,
//                boundingBox.getLonWest() * 2 - centerLong
//
//        );

        return boundingBox;
    }


    private void addNewMapItem(Set<? extends Cluster<GeoObjectItem>> clusters) {

        List<GeoObjectItem> geoObjectItems = new ArrayList<>();
        for (Cluster<GeoObjectItem> cluster : clusters) {

            Collection<GeoObjectItem> items = cluster.getItems();
            if (cluster.getSize() <50) {
                geoObjectItems.addAll(items);
            } else {



                releaseClustedItems(cluster.getItems());
//                List<GeoObjectItem> list = (List<GeoObjectItem>) cluster.getItems();
//                List<GeoPoint> latLngs = new ArrayList<>();
//                latLngs.add(list.get(0).getPosition());
//                latLngs.add(list.get(list.size() - 1).getPosition());
//
//                geoObjectItems.add(new GeoObjectItem(new KmlLineString(latLngs), String.valueOf(cluster.getSize()), CLUSTER));
                geoObjectItems.add(new GeoObjectItem(cluster.getPosition(), String.valueOf(cluster.getSize()), CLUSTER));
            }


        }

        for (GeoObjectItem geoObjectItem : geoObjectItems) {

            addMapItem(geoObjectItem);

        }
        mapView.invalidate();


    }

    private void releaseClustedItems(Collection<GeoObjectItem> items) {


        generator.releaseItems(items);




    }

    private void addMapItem(GeoObjectItem geoObjectItem) {


        KmlPlacemark placemark = geoObjectItem.mGeometry == null ? null : kmlHelper.kmlPlacemarkSparseArray.get(geoObjectItem.mGeometry.hashCode());


        OverlayWithIW generate = generator.generate(geoObjectItem, mapView, null, null, placemark, kmlDocument);


        if (generate == null) return;


        if (!folderOverlay.getItems().contains(generate)) {
            folderOverlay.add(generate);
        }


    }


    private class ModifyViewHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_RENDER:
                    if (mClusterTask != null && !mClusterTask.isCancelled()) {
                        try {
                            mClusterTask.cancel(true);
                        } catch (Throwable t) {
                        }
                    }

                    mClusterTask = new ClusterTask();
                    mClusterTask.execute(mapView.getZoomLevel(), getVisibleLatLngBounds());
                    break;
            }

            super.handleMessage(msg);
        }
    }


}
