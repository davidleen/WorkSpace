package com.rnmap_wb.activity.mapwork;

import com.giants3.android.mvp.Viewer;
import com.rnmap_wb.entity.MapElement;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public interface MapWorkViewer extends Viewer {
    void showPolyLine(List<GeoPoint> polyLinePositions);

    void showCircle(GeoPoint center, double radio);

    void showRectangle(List<GeoPoint> rectangle);

    void startDownLoadTask(Long taskId);

    void showMapElement(MapElement element);

    void removeMapElement(MapElement replaced);
}
