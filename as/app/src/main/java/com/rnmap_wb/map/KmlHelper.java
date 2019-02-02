package com.rnmap_wb.map;

import android.util.SparseArray;

import com.giants3.android.frame.util.Log;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlFolder;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlMultiGeometry;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.util.GeoPoint;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class KmlHelper {


    SparseArray<KmlPlacemark> kmlPlacemarkSparseArray = new SparseArray<>();


    public final List<KmlGeometry> getAllKmlGeometry(KmlDocument document) {

        kmlPlacemarkSparseArray.clear();

        ArrayList<KmlGeometry> latLngs = new ArrayList<>();

        loadAllGeometry(document, latLngs);
        return latLngs;


    }

    private void loadAllGeometry(KmlDocument document, List<KmlGeometry> allLatlngs) {


        addAllGeometryInContainer(document.mKmlRoot.mItems, allLatlngs);


    }

    private void addAllGeometryInContainer(List<KmlFeature> containers, List<KmlGeometry> latLngs) {


        for (KmlFeature item : containers) {

            if (item instanceof KmlFolder) {
                addAllGeometryInContainer(((KmlFolder) item).mItems, latLngs);
            }
            if (item instanceof KmlPlacemark) {
                addAllGeometry(item, ((KmlPlacemark) item).mGeometry, latLngs);

            }


//            if(item instanceof KmlGroundOverlay)
//            {
//                addAllGeometry(item,   ((KmlGroundOverlay) item).,latLngs);
////            }
//            for (item in feature : item.hasGeometry())) {
//
//                addAllGeometry(feature, feature.getGeometry(), latLngs);
//            }


        }


    }

    private void addAllGeometry(KmlFeature feature, KmlGeometry geometry, List<KmlGeometry> latLngs) {

        if (feature instanceof KmlPlacemark)
            kmlPlacemarkSparseArray.put(geometry.hashCode(), (KmlPlacemark) feature);
        if (geometry instanceof KmlMultiGeometry) {

            for (KmlGeometry item : ((KmlMultiGeometry) geometry).mItems) {
                addAllGeometry(feature, item, latLngs);
            }
        } else
            latLngs.add(geometry);

    }

    /**
     * Creates a new XmlPullParser to allow for the KML file to be parsed
     *
     * @param stream InputStream containing KML file
     * @return XmlPullParser containing the KML file
     * @throws XmlPullParserException if KML file cannot be parsed
     */
    private static XmlPullParser createXmlParser(InputStream stream) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(stream, null);
        return parser;
    }


    private static void addAll(KmlGeometry geometry, List<GeoPoint> latLngs) {

        if (geometry != null) {

            if (geometry instanceof KmlMultiGeometry) {
                List<KmlGeometry> geometries = ((KmlMultiGeometry) geometry).mItems;
                Log.e("geoMetry Size：" + geometries.size());
                for (KmlGeometry item : geometries) {
                    addAll(item, latLngs);
                    Log.e("item.type" + item.mCoordinates);
                }

            } else if (geometry instanceof KmlPolygon) {
                latLngs.addAll(((KmlPolygon) geometry).mCoordinates);
            } else if (geometry instanceof KmlLineString) {
                latLngs.addAll(((KmlLineString) geometry).mCoordinates);
            } else if (geometry instanceof KmlPoint) {
                latLngs.add(((KmlPoint) geometry).getPosition());
            }


        }

    }


}
