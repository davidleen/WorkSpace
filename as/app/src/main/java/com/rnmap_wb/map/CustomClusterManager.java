package com.rnmap_wb.map;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

import com.giants3.android.frame.util.Log;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;
import com.rnmap_wb.activity.mapwork.GeoObjectItem;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.activity.mapwork.map.KMlMarker;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.TileSystem;
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
    public static final int MSG_FILTER = 112;
    public static final String CLUSTER = "放大查看";

    private int lastZoom = -100;

    private List<GeoObjectItem> geoObjectItems;
    SparseArray<List<GeoObjectItem>> objectItemSparseArray;

    Handler handler;
    Algorithm mAlgorithm;
    private MapView mapView;
    private KmlDocument kmlDocument;
    private MapWorkActivity.OverlayWithIWClickListener clickListener;
    ClusterTask mClusterTask;
    AsyncTask<Void, Void, List<GeoObjectItem>> filterTask;
    private final ReadWriteLock mAlgorithmLock = new ReentrantReadWriteLock();

    private GeoObjectItemGenerator generator;

    FolderOverlay folderOverlay;

    public CustomClusterManager(MapView map, KmlDocument kmlDocument, MapWorkActivity.OverlayWithIWClickListener clickListener) {
        this.mapView = map;
        this.kmlDocument = kmlDocument;
        this.clickListener = clickListener;
        objectItemSparseArray = new SparseArray<>();

        mapView.addMapListener(this);
        generator = new GeoObjectItemGenerator();
        mAlgorithm = new PreCachingAlgorithmDecorator<GeoObjectItem>(new NonHierarchicalDistanceBasedAlgorithm<GeoObjectItem>());
        handler = new ModifyViewHandler();


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

        handler.removeMessages(MSG_RENDER);
        handler.sendEmptyMessageDelayed(MSG_RENDER, 500);
        return false;

    }

    /**
     * Runs the clustering algorithm in a background thread, then re-paints when results come back.
     */
    private class ClusterTask extends AsyncTask<Object, Void, List<GeoObjectItem>> {

        private int zoomLevel;

        public ClusterTask(int zoomLevel) {

            this.zoomLevel = zoomLevel;
        }

        @Override
        protected List<GeoObjectItem> doInBackground(Object... zoom) {
            mAlgorithmLock.readLock().lock();
            try {
                if (isCancelled()) return null;
                mAlgorithm.clearItems();

                //过滤不再显示范围内的集合。
                List<GeoObjectItem> items = new ArrayList<>();

                items.addAll(geoObjectItems);

                mAlgorithm.addItems(items);

                Set<Cluster<GeoObjectItem>> clusters = mAlgorithm.getClusters(zoomLevel);

                if (isCancelled()) return null;


                List<GeoObjectItem> result = new ArrayList<>();
                for (Cluster<GeoObjectItem> cluster : clusters) {

                    Collection<GeoObjectItem> clusterItems = cluster.getItems();
                    if (cluster.getSize() < 50 || mapView.getZoomLevel() > 15) {
                        result.addAll(clusterItems);
                    } else {

                        int index = 0;
                        for (GeoObjectItem item : clusterItems) {
                            if (index < 1 && item.mGeometry instanceof KmlPoint) {
                                result.add(item);
                                index++;

                            }

                        }
                    }


                }


                return result;
            } finally {
                mAlgorithmLock.readLock().unlock();
            }
        }

        @Override
        protected void onPostExecute(List<GeoObjectItem> items) {

            if (items == null) return;
            objectItemSparseArray.put(zoomLevel, items);
            handler.removeMessages(MSG_FILTER);
            handler.sendEmptyMessageDelayed(MSG_FILTER,500);


        }
    }


    private void filterItems(final List<GeoObjectItem> items) {

        if (items == null) return;

        if (filterTask != null && !filterTask.isCancelled()) {
            try {
                filterTask.cancel(true);
            } catch (Throwable t) {
            }
        }


        final BoundingBox boundingBox = getVisibleLatLngBounds();

        filterTask = new AsyncTask<Void, Void, List<GeoObjectItem>>() {
            @Override
            protected List<GeoObjectItem> doInBackground(Void... voids) {


                List<GeoObjectItem> result = new ArrayList<>();

                for (GeoObjectItem geoObjectItem : items) {

                    if(isCancelled()) return null;
                    if (geoObjectItem.isInBounds(boundingBox))
                        result.add(geoObjectItem);
                }


                return result;
            }

            @Override
            protected void onPostExecute(List<GeoObjectItem> geoObjectItems) {
                super.onPostExecute(geoObjectItems);
                if(geoObjectItems==null)return ;
                if (isCancelled()) {
                    return;
                }
                addNewMapItem(geoObjectItems);

            }
        };
        filterTask.execute();


    }

    private void onNewCluster(Set<? extends Cluster<GeoObjectItem>> clusters) {

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

    }

    public void addItems(List<GeoObjectItem> geoObjectItems) {

        this.geoObjectItems = geoObjectItems;

    }


    private BoundingBox getVisibleLatLngBounds() {
        BoundingBox boundingBox = mapView.getBoundingBox();

        double centerLat = boundingBox.getCenterLatitude();
        double centerLong = boundingBox.getCenterLongitude();
        TileSystem tileSystem = mapView.getTileSystem();


        BoundingBox doubleBox = new BoundingBox(Math.min(boundingBox.getLatNorth() * 2 - centerLat, tileSystem.getMaxLatitude()),
                Math.max(boundingBox.getLonEast() * 2 - centerLong, tileSystem.getMinLongitude()),
                Math.max(boundingBox.getLatSouth() * 2 - centerLat, tileSystem.getMinLatitude()),
                Math.min(boundingBox.getLonWest() * 2 - centerLong, tileSystem.getMaxLongitude())

        );
        boundingBox = doubleBox;
        return boundingBox;
    }


    private void addNewMapItem(Set<? extends Cluster<GeoObjectItem>> clusters) {

        List<GeoObjectItem> geoObjectItems = new ArrayList<>();
        for (Cluster<GeoObjectItem> cluster : clusters) {

            Collection<GeoObjectItem> items = cluster.getItems();
            if (cluster.getSize() < 50 || mapView.getZoomLevel() > 15) {
                geoObjectItems.addAll(items);
            } else {


//                releaseClustedItems(cluster.getItems());
////                List<GeoObjectItem> list = (List<GeoObjectItem>) cluster.getItems();
////                for(geo)
////                List<GeoPoint> latLngs = new ArrayList<>();
////                latLngs.add(list.get(0).getPosition());
////                latLngs.add(list.get(list.size() - 1).getPosition());
////
////                geoObjectItems.add(new GeoObjectItem(new KmlLineString(latLngs), String.valueOf(cluster.getSize()), CLUSTER));
//                geoObjectItems.add(new GeoObjectItem(cluster.getPosition(), String.valueOf(cluster.getSize()), CLUSTER));

                ;
                List<GeoObjectItem> others = new ArrayList<>();
                others.addAll(items);
                GeoObjectItem first = null;
                for (GeoObjectItem item : others) {
                    if (item.mGeometry instanceof KmlPoint) {
                        first = item;
                        break;
                    }
                }
                if (first != null) {
                    others.remove(first);
                    geoObjectItems.add(first);
                }

                releaseClustedItems(others);


            }


        }


        List<OverlayWithIW> generate = generator.updateItems(geoObjectItems, mapView, null, null, kmlDocument, kmlHelper);

        for (OverlayWithIW iw : generate) {

            if (iw instanceof KMlMarker) {
                ((KMlMarker) iw).bindData();
            }
            if (!folderOverlay.getItems().contains(iw)) {
                folderOverlay.add(iw);
            }
        }


        folderOverlay.setEnabled(true);
        mapView.invalidate();


    }

    private void addNewMapItem(List<GeoObjectItem> items) {


        List<OverlayWithIW> generate = generator.updateItems(items, mapView, null, null, kmlDocument, kmlHelper);

        for (OverlayWithIW iw : generate) {

            if (!folderOverlay.getItems().contains(iw)) {
                folderOverlay.add(iw);
            }
        }


        folderOverlay.setEnabled(true);
        mapView.invalidate();


    }

    private void releaseClustedItems(Collection<GeoObjectItem> items) {


        generator.releaseItems(items);


    }


    private class ModifyViewHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {

            int zoomLevel = mapView.getZoomLevel();
            switch (msg.what) {


                case MSG_FILTER:


                    filterItems(objectItemSparseArray.get(zoomLevel));


                    break;
                case MSG_RENDER:

                    removeMessages(MSG_FILTER);
                    if (filterTask != null && !filterTask.isCancelled()) {
                        try {
                            filterTask.cancel(true);
                        } catch (Throwable t) {
                        }
                    }

                    if (mClusterTask != null && !mClusterTask.isCancelled()) {
                        try {
                            mClusterTask.cancel(true);
                        } catch (Throwable t) {
                        }
                    }

                    if (objectItemSparseArray.get(zoomLevel) != null) {

                        sendEmptyMessageDelayed(MSG_FILTER, 500);
                        return;
                    }

                    mClusterTask = new ClusterTask(zoomLevel);
                    mClusterTask.execute();
                    lastZoom = zoomLevel;
                    break;
            }

            super.handleMessage(msg);
        }
    }


}
