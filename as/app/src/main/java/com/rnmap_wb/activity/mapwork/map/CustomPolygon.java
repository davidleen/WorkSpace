package com.rnmap_wb.activity.mapwork.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;

import com.rnmap_wb.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import java.util.List;

public class CustomPolygon extends Polygon {


    public CustomPolygon() {
        super();
        setFillColor(Color.parseColor("#224754fd"));

        setStrokeColor(Color.YELLOW);
    }

    Point point =new Point();
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);

        List<GeoPoint> points = getPoints();


        Drawable drawable_point=  ContextCompat.getDrawable(mapView.getContext(),R.drawable.icon_map_point);
        drawable_point.setBounds(0,0,drawable_point.getIntrinsicWidth(),drawable_point.getIntrinsicHeight());
        int size = points.size();

        for (int i = 0; i < size; i++) {
            GeoPoint geoPoint = points.get(i);
            mapView.getProjection().toPixels(geoPoint, point);
            canvas.save();
            canvas.translate(point.x - drawable_point.getIntrinsicWidth() / 2, point.y - drawable_point.getIntrinsicHeight() / 2);

            drawable_point.draw(canvas);

            canvas.restore();





        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        return super.onTouchEvent(event, mapView);
    }
}
