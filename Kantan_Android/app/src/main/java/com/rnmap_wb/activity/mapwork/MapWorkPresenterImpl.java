package com.rnmap_wb.activity.mapwork;

import android.os.AsyncTask;

import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.frame.util.Log;
import com.giants3.android.mvp.BasePresenter;
import com.giants3.android.reader.domain.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.android.idao.DaoManager;
import com.rnmap_wb.android.idao.IDownloadTaskDao;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.android.entity.DownloadItem;
import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.entity.MapElement;
import com.rnmap_wb.map.TileUtil;
import com.rnmap_wb.service.SynchronizeCenter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.rnmap_wb.entity.MapElement.TYPE_CIRCLE;
import static com.rnmap_wb.entity.MapElement.TYPE_KML_MARK;
import static com.rnmap_wb.entity.MapElement.TYPE_POLYGON;
import static com.rnmap_wb.entity.MapElement.TYPE_POLYLINE;

public class MapWorkPresenterImpl extends BasePresenter<MapWorkViewer, MapWorkModel> implements MapWorkPresenter {


    private Task task;

    @Override
    public void addNewMapElement(MapElement element) {

        getModel().addNewMapElement(element);
        getModel().setEditElement(element);
        updateMapElementCache();
        getView().showMapElement(element);
    }

    private void updateMapElementCache() {

        String filePath = SynchronizeCenter.getElementsFilePath(task);
        List<MapElement> elements = getModel().getMapElements();

        List<MapElement> elementToSave = new ArrayList<>();
        for (MapElement mapElement : elements) {

            if ((mapElement.type == MapElement.TYPE_MAPPING_LINE || mapElement.type == MapElement.TYPE_POLYLINE) && LatLngUtil.convertStringToGeoPoints(mapElement.latLngs).size() <= 1) {
                continue;
            }
            elementToSave.add(mapElement);
        }

        try {
            FileUtils.writeStringToFile(GsonUtils.toJson(elementToSave), filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void downloadMap(final List<GeoPoint> latLngs, final int fromZoom, final int toZoom) {


        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                DaoManager.getInstance().beginTransaction();
                try {


                    beginDownLoadTask(latLngs, fromZoom, toZoom);
                    DaoManager.getInstance().commitTransaction();
                } catch (Throwable t) {
                    Log.e(t);
                } finally {

                    DaoManager.getInstance().endTransaction();
                }
                return null;

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        getView().showMessage("当前区域已经加入下载队列中");

    }

    private void beginDownLoadTask(List<GeoPoint> latLngs, int fromZoom, int toZoom) {
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.setCreateTime(Calendar.getInstance().getTime().toString());
        downloadTask.setName("地图区域下载");

        downloadTask.setLatLngs(LatLngUtil.convertGeoPointToString(latLngs));
        downloadTask.setFromZoom(fromZoom);
        downloadTask.setToZoom(toZoom);
        double fromLat = latLngs.get(0).getLatitude();
        double fromLng = latLngs.get(0).getLongitude();
        double toLat = latLngs.get(1).getLatitude();
        double toLng = latLngs.get(1).getLongitude();

        IDownloadTaskDao downloadTaskDao;
        downloadTaskDao = DaoManager.getInstance().getDownloadTaskDao();
        Long id = downloadTaskDao.insert(downloadTask);
        downloadTask.setId(id);
        List<DownloadItem> downloadItems = new ArrayList<>();
        Random random = new Random();
        int[] startXY = new int[2];
        int[] endXY = new int[2];
        int maxZoom = 12;
        int minZoom = 11;
        int totalCount = 0;
        for (int z = fromZoom; z <= toZoom; z++) {


            LatLngUtil.getTileNumber(fromLat, fromLng, z, startXY);


            LatLngUtil.getTileNumber(toLat, toLng, z, endXY);


            int maxX = Math.max(startXY[0], endXY[0]);
            int minX = Math.min(startXY[0], endXY[0]);


            for (int x = minX; x <= maxX; x++) {

                int maxY = Math.max(startXY[1], endXY[1]);
                int minY = Math.min(startXY[1], endXY[1]);
                for (int y = minY; y <= maxY; y++) {

                    DownloadItem downloadItem = new DownloadItem();
                    downloadItem.setTaskId(id);
                    downloadItem.setName("  fromLat:" + fromLat + ", fromLng:" + fromLng + ",   toLat :" + toLat + ",   toLng：" + toLng);
                    downloadItem.setTileX(x);
                    downloadItem.setTileY(y);
                    downloadItem.setTileZ(z);

                    downloadItem.setDownloadFilePath(TileUtil.getFilePath(x, y, z));
                    downloadItems.add(downloadItem);
                    totalCount++;

                    if (downloadItems.size() > 1000) {
                        DaoManager.getInstance().getDownloadItemDao().saveAll(downloadItems);
                        downloadItems.clear();
                    }
                }


            }
        }

        DaoManager.getInstance().getDownloadItemDao().saveAll(downloadItems);

        downloadTask.count = totalCount;
        downloadTaskDao.save(downloadTask);

        getView().startDownLoadTask(id);
    }


    @Override
    public void addNewPolylinePoint(GeoPoint p) {


//        MapElement mapElement=new MapElement();
//        mapElement.type=3;
//        mapElement.latLngs=LatLngUtil.convertLatLngToString(latLng)


        MapElement mapElement = getModel().getEdittingMapElement();

        if (mapElement == null || mapElement.type != TYPE_POLYLINE) {
            mapElement = new MapElement();
            mapElement.type = TYPE_POLYLINE;
            getModel().addNewMapElement(mapElement);
            getModel().setEditElement(mapElement);
        }

        List<GeoPoint> geoPoints = LatLngUtil.convertStringToGeoPoints(mapElement.latLngs);
        geoPoints.add(p);
        String s = LatLngUtil.convertGeoPointToString(geoPoints);
        mapElement.latLngs = s;
        updateMapElementCache();
        getView().showMapElement(mapElement);


    }

    @Override
    public void addNewPolyline(GeoPoint start, GeoPoint end) {
        MapElement


                mapElement = new MapElement();
        mapElement.type = TYPE_POLYLINE;
        getModel().addNewMapElement(mapElement);
        getModel().setEditElement(mapElement);


        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(start);
        geoPoints.add(end);
        String s = LatLngUtil.convertGeoPointToString(geoPoints);
        mapElement.latLngs = s;
        updateMapElementCache();
        getView().showMapElement(mapElement);


    }

    @Override
    public void addNewCircle(GeoPoint latLng, double radius) {


        MapElement mapElement = new MapElement();
        mapElement.type = TYPE_CIRCLE;
        ArrayList<GeoPoint> latLngs = new ArrayList<>();
        latLngs.add(latLng);
        mapElement.latLngs = LatLngUtil.convertGeoPointToString(latLngs);
        mapElement.radius = radius;

        getModel().setEditElement(mapElement);
        getModel().addNewMapElement(mapElement);
        updateMapElementCache();
        getView().showMapElement(mapElement);

    }

    @Override
    public void addNewRectangle(List<GeoPoint> latLngs) {

        String s = LatLngUtil.convertGeoPointToString(latLngs);
        MapElement mapElement = new MapElement();
        mapElement.latLngs = s;
        mapElement.type = TYPE_POLYGON;
        getModel().addNewMapElement(mapElement);
        getModel().setEditElement(mapElement);
        updateMapElementCache();
        getView().showMapElement(mapElement);
    }


    @Override
    public void clearMapElements() {


        getModel().clearEmelemnts();
        getModel().setEditElement(null);
        updateMapElementCache();


    }

    @Override
    public void clearMappingElements() {


        MapElement edittingMapElement = getModel().getEdittingMapElement();
        if (edittingMapElement!=null&&(edittingMapElement.type == MapElement.TYPE_MAPPING_LINE_DEGREE || edittingMapElement.type == MapElement.TYPE_MAPPING_LINE))
             getView().removeMapElement(edittingMapElement);

        List<MapElement> mapElements = getModel().getMapElements();
        List<MapElement> mappings = new ArrayList<>();
        for (MapElement mapElement : mapElements) {
            if (mapElement.type == MapElement.TYPE_MAPPING_LINE_DEGREE || mapElement.type == MapElement.TYPE_MAPPING_LINE) {
                mappings.add(mapElement);
            }
        }

        mapElements.removeAll(mappings);
        getModel().setEditElement(null);
        updateMapElementCache();

        for (MapElement mapElement : mappings) {
            getView().removeMapElement(mapElement);
        }


    }

    @Override
    public void removeElement(MapElement mapElement) {


        getModel().removeElement(mapElement);
        updateMapElementCache();


    }

    @Override
    public MapWorkModel createModel() {
        return new MapWorkModelImpl();
    }


    @Override
    public void start() {

    }


    @Override
    public void prepare(Task task) {
        this.task = task;
        if (task == null) return;

        //查找本地文件  是否存在标记文件
        String filePath = SynchronizeCenter.getElementsFilePath(task);

        String mapElementsString = FileUtils.readStringFromFile(filePath);
        List<MapElement> mapElements = GsonUtils.fromJson(mapElementsString, new TypeToken<List<MapElement>>() {
        }.getType());
        if (mapElements == null)
            mapElements = new ArrayList<>();
        getModel().setMapElements(mapElements);


    }


    @Override
    public void onMapPrepare() {


        List<MapElement> mapElements = getModel().getMapElements();

        for (MapElement element : mapElements)
            getView().showMapElement(element);
    }

    @Override
    public void closePaint() {


        List<GeoPoint> positions = getModel().getPolyLinePositions();

        String s = LatLngUtil.convertGeoPointToString(positions);
        MapElement mapElement = new MapElement();
        mapElement.type = TYPE_POLYLINE;
        mapElement.latLngs = s;
        getModel().addNewMapElement(mapElement);
        getModel().clearPolyLinePosition();

        updateMapElementCache();
        getView().showMapElement(mapElement);

    }

    @Override
    public void updateMapElement(MapElement o) {

        MapElement replaced = getModel().updateMapElement(o);
        if (replaced != null) {
            getView().removeMapElement(replaced);
        }
        updateMapElementCache();
        getView().showMapElement(o);
    }

    @Override
    public void requestFeekBack(String pointString) {


        MapElement mapElement = getModel().getMapElementByPoint(pointString);
        if (mapElement == null) {
            mapElement = new MapElement();
            mapElement.type = TYPE_KML_MARK;
            mapElement.latLngs = pointString;

        }

        getView().feedBack(mapElement);


    }

    @Override
    public void addMappingLine(GeoPoint p) {


        addMappingTypeLine(p, MapElement.TYPE_MAPPING_LINE);


    }

    private void addMappingTypeLine(GeoPoint p, int elementType) {
        MapElement mapElement = getModel().getEdittingMapElement();

        getView().removeMapElement(mapElement);
        if (mapElement == null || mapElement.type != elementType) {
            mapElement = new MapElement();
            mapElement.type = elementType;
            getModel().addNewMapElement(mapElement);
            getModel().setEditElement(mapElement);
        }

        List<GeoPoint> geoPoints = LatLngUtil.convertStringToGeoPoints(mapElement.latLngs);
        geoPoints.add(p);
        String s = LatLngUtil.convertGeoPointToString(geoPoints);
        mapElement.latLngs = s;
        updateMapElementCache();
        getView().showMapElement(mapElement);
    }

    @Override
    public void addMappingRadius(GeoPoint p) {

        addMappingTypeLine(p, MapElement.TYPE_MAPPING_LINE_DEGREE);


    }

    @Override
    public void addTracking(List<GeoPoint> points) {
        MapElement mapElement = new MapElement();
        mapElement.type = MapElement.TYPE_TRACK_LINE;
        String s = LatLngUtil.convertGeoPointToString(points);
        mapElement.latLngs = s;
        getModel().addNewMapElement(mapElement);
        updateMapElementCache();
        getView().showMapElement(mapElement);

    }
}
