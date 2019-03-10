package com.rnmap_wb.map;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.MainApplication;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.GeoObjectItem;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.activity.mapwork.map.CustomMarker;

import org.osmdroid.bonuspack.kml.IconStyle;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rnmap_wb.map.CustomClusterManager.CLUSTER;

public class GeoObjectItemGenerator {


    Map<Object, Object> geoItemToMap = new HashMap<>();
    Map<Object, Object> mapToGeoItem = new HashMap<>();

    private List<OverlayWithIW> visibleObjects = new ArrayList<>();
    private List<Marker> markerBin = new ArrayList<>();
    private List<Polyline> polylineBin = new ArrayList<>();
    private List<Polygon> polygonBin = new ArrayList<>();

    public OverlayWithIW generate(GeoObjectItem item, MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlPlacemark kmlPlacemark,
                                  KmlDocument kmlDocument) {


        //标记已经存在界面上 不添加
        if (geoItemToMap.containsKey(item)) return null;
//        if (item.mGeometry==null ) {
//            for (OverlayWithIW iw:visibleObjects)
//            {
//                if(iw instanceof Marker)
//                {
//                    if (((Marker) iw).getPosition().equals(item.getPosition())) {
//                        return null;
//                    }
//                }
//            }
//
//        }


        OverlayWithIW result = null;
        if (item.mGeometry instanceof KmlLineString) {
            result = buildOverlay((KmlLineString) item.mGeometry, map, defaultStyle, styler, kmlPlacemark, kmlDocument);

        } else if (item.mGeometry instanceof KmlPoint) {
            ///result=buildOverlay()
            result = buildOverlay((KmlPoint) item.mGeometry, map, defaultStyle, styler, kmlPlacemark, kmlDocument);

        } else if (item.mGeometry instanceof KmlPolygon) {
            ///result=buildOverlay()
            result = buildOverlay((KmlPolygon) item.mGeometry, map, defaultStyle, styler, kmlPlacemark, kmlDocument);

        } else if (item.mGeometry == null) {
            result = buildOverlay(item, map, defaultStyle, styler, kmlPlacemark, kmlDocument);
        }


        if (result != null) {

            mapToGeoItem.put(result, item);
            geoItemToMap.put(item, result);
            visibleObjects.add(result);
        }


        return result;
    }


    /**
     * Build the corresponding Polyline overlay
     */
    public OverlayWithIW buildOverlay(KmlLineString kmlLineString, MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlPlacemark kmlPlacemark,
                                      KmlDocument kmlDocument) {
        Polyline lineStringOverlay = cratePolyLine();
        lineStringOverlay.setGeodesic(true);
        lineStringOverlay.setPoints(kmlLineString.mCoordinates);
        lineStringOverlay.setTitle(kmlPlacemark == null ? "" : kmlPlacemark.mName);
        lineStringOverlay.setSnippet(kmlPlacemark == null ? "" : kmlPlacemark.mDescription);
        lineStringOverlay.setSubDescription(kmlPlacemark.getExtendedDataAsText());
        lineStringOverlay.setRelatedObject(kmlLineString);
        lineStringOverlay.setId(kmlLineString.mId);
        if (styler != null)
            styler.onLineString(lineStringOverlay, kmlPlacemark, kmlLineString);
        else {
            kmlLineString.applyDefaultStyling(lineStringOverlay, defaultStyle, kmlPlacemark, kmlDocument, map);
        }
        lineStringOverlay.setVisible(true);
        return lineStringOverlay;
    }

    /**
     * Build the corresponding Polygon overlay
     */
    public OverlayWithIW buildOverlay(KmlPolygon kmlPolygon, MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlPlacemark kmlPlacemark,
                                      KmlDocument kmlDocument) {
        Polygon polygonOverlay = createPolygon();
        polygonOverlay.setPoints(kmlPolygon.mCoordinates);
        if (kmlPolygon.mHoles != null)
            polygonOverlay.setHoles(kmlPolygon.mHoles);
        polygonOverlay.setTitle(kmlPlacemark == null ? "" : kmlPlacemark.mName);
        polygonOverlay.setSnippet(kmlPlacemark == null ? "" : kmlPlacemark.mDescription);
        polygonOverlay.setSubDescription(kmlPlacemark == null ? "" : kmlPlacemark.getExtendedDataAsText());
        polygonOverlay.setRelatedObject(this);
        polygonOverlay.setId(kmlPolygon.mId);
        if (styler == null)
            kmlPolygon.applyDefaultStyling(polygonOverlay, defaultStyle, kmlPlacemark, kmlDocument, map);
        else
            styler.onPolygon(polygonOverlay, kmlPlacemark, kmlPolygon);
        polygonOverlay.setVisible(true);
        return polygonOverlay;
    }

    /**
     * Build the corresponding Marker overlay
     */
    public OverlayWithIW buildOverlay(KmlPoint kmlPoint, MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlPlacemark kmlPlacemark,
                                      KmlDocument kmlDocument) {
        Marker marker = createMarker(map);
        marker.setTitle(kmlPlacemark == null ? "" : kmlPlacemark.mName);
        marker.setSnippet(kmlPlacemark == null ? "" : kmlPlacemark.mDescription);
        marker.setSubDescription(kmlPlacemark == null ? "" : kmlPlacemark.getExtendedDataAsText());
        marker.setPosition(kmlPoint.getPosition());
        //keep the link from the marker to the KML feature:
        marker.setRelatedObject(this);
        marker.setId(kmlPoint.mId);


        if (styler == null) {
            if (marker instanceof CustomMarker) {
                IconStyle iconStyle = kmlPoint.getIconStyle(defaultStyle, kmlPlacemark, kmlDocument);
                ((CustomMarker) marker).applyIconStyle(iconStyle);

            } else {
                kmlPoint.applyDefaultStyling(marker, defaultStyle, kmlPlacemark, kmlDocument, map);
            }
        } else
            styler.onPoint(marker, kmlPlacemark, kmlPoint);

        marker.setVisible(true);
        return marker;
    }

    /**
     * Build the corresponding Marker overlay
     */
    public OverlayWithIW buildOverlay(GeoObjectItem geoObjectItem, MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlPlacemark kmlPlacemark,
                                      KmlDocument kmlDocument) {
        Marker marker = createMarker(map);
        marker.setTitle(geoObjectItem.getTitle());
        marker.setSnippet(geoObjectItem.getSnippet());
        marker.setSubDescription(kmlPlacemark == null ? "" : kmlPlacemark.getExtendedDataAsText());
        marker.setPosition(geoObjectItem.getPosition());
        //keep the link from the marker to the KML feature:
        marker.setRelatedObject(geoObjectItem);
        marker.setVisible(true);

        if (marker instanceof CustomMarker) {
            ((CustomMarker) marker).applyIconStyle(null);
        }


        return marker;
    }

    private Marker createMarker(MapView mapView) {
        Marker marker = null;

        int size = markerBin.size();
        if (size > 0) {
            marker = markerBin.remove(size - 1);
        } else {

            marker = new Marker(mapView);

        }


        marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.icon_map_point));
        return marker;

    }

    @NonNull
    private Polyline cratePolyLine() {
        Polyline polyline;
        int size = polylineBin.size();
        if (size > 0) {
            polyline = polylineBin.remove(size - 1);
        } else

            polyline = new Polyline();
        return polyline;
    }

    @NonNull
    private Polygon createPolygon() {
        Polygon polygon;
        int size = polygonBin.size();
        if (size > 0) {
            polygon = polygonBin.remove(size - 1);
        } else

            polygon = new Polygon();
        return polygon;
    }

    public void releaseOverlayIw(OverlayWithIW iw) {
        if (iw instanceof Polyline) {
            Polyline polyline = (Polyline) iw;
            polylineBin.add(polyline);
            polyline.setVisible(false);
        } else if (iw
                instanceof Polygon) {
            Polygon polygon = (Polygon) iw;
            polygonBin.add(polygon
            );
            polygon.setVisible(false);
        } else if (iw instanceof Marker) {
            Marker marker = (Marker) iw;
            markerBin.add(marker);
            marker.setVisible(false);
        }


    }


    private List<OverlayWithIW> tempList = new ArrayList<>();

    public void clearUnVisibleItems(BoundingBox visibleLatLngBounds) {

        {
            tempList.clear();
            for (OverlayWithIW iw : visibleObjects) {
                GeoObjectItem item = (GeoObjectItem) mapToGeoItem.get(iw);
                if (iw instanceof Marker) {

                    if (item!=null&&item.mGeometry!=null&&visibleLatLngBounds.contains(((Marker) iw).getPosition())) {
                        tempList.add(iw);

                    } else {
                        ((Marker) iw).setVisible(false);
                        clearMapItem(iw);
                        releaseOverlayIw(iw);

                    }
                } else if (iw instanceof Polyline) {
                    List<GeoPoint> points = ((Polyline) iw).getPoints();
                    if (  LatLngUtil.linesIntersectRect(points, visibleLatLngBounds)) {
                        tempList.add(iw);
                    } else {
                        ((Polyline) iw).setVisible(false);
                        clearMapItem(iw);
                        releaseOverlayIw(iw);

                    }
                } else if (iw instanceof Polygon) {
                    List<GeoPoint> points = ((Polygon) iw).getPoints();
                    if (  LatLngUtil.linesIntersectRect(points, visibleLatLngBounds)) {
                        tempList.add(iw);
                    } else {
                        ((Polygon) iw).setVisible(false);
                        clearMapItem(iw);
                        releaseOverlayIw(iw);

                    }

                }


            }


            visibleObjects.clear();
            visibleObjects.addAll(tempList);
        }


    }

    private void clearMapItem(Object mapItem) {
        Object geoItem = mapToGeoItem.remove(mapItem);
        geoItemToMap.remove(geoItem);

    }

    /**
     * Build a FolderOverlay, containing (recursively) overlays from all items of this Folder.
     *
     * @param map
     * @param defaultStyle to apply when an item has no Style defined.
     * @param styler       to apply
     * @param kmlDocument  for Styles
     * @return the FolderOverlay built
     */
    public FolderOverlay buildFolderOverlay(MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlDocument kmlDocument) {
        FolderOverlay folderOverlay = new FolderOverlay();
        folderOverlay.setName(kmlDocument.mKmlRoot.mName);
        folderOverlay.setDescription(kmlDocument.mKmlRoot.mDescription);

        if (styler == null)
            folderOverlay.setEnabled(kmlDocument.mKmlRoot.mVisibility);
        else
            styler.onFeature(folderOverlay, kmlDocument.mKmlRoot);
        return folderOverlay;
    }

    public void releaseItems(Collection<GeoObjectItem> items) {

        for (GeoObjectItem geoObjectItem : items) {
            Object mapItem = geoItemToMap.remove(geoObjectItem);
            mapToGeoItem.remove(mapItem);

            releaseOverlayIw((OverlayWithIW) mapItem);

        }


    }
}
