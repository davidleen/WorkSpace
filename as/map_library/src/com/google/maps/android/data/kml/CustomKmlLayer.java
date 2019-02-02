package com.google.maps.android.data.kml;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.MultiGeometry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CustomKmlLayer extends KmlLayer {
    DynamicKmlRenderer renderer;

    public CustomKmlLayer(GoogleMap map, int resourceId, Context context) throws XmlPullParserException, IOException {
        super(map, resourceId, context);
        init();
    }

    public CustomKmlLayer(GoogleMap map, InputStream stream, Context context) throws XmlPullParserException, IOException {
        super(map, stream, context);
        init();
    }

    private void init()
    {


    }



    @NonNull
    protected KmlRenderer createRenderer(GoogleMap map, Context context) {
        renderer= new DynamicKmlRenderer(map, context);
        return renderer;
    }

    public void updateCamera() {

        renderer.onCameraUpdate();
    }
}
