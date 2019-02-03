package com.rnmap_wb.activity.mapwork;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;

public class Circle extends OverlayWithIW {
    private GeoPoint center;
    private boolean clickable;
    private double radius;

    Point  point =new Point();
    Paint paint=new Paint();
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {


        osmv.getProjection().toPixels(center,point);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);

        c.drawCircle(point.x,point.y,100,paint);

    }

    public void setRadius(double radius) {

        this.radius = radius;
    }

    public void setCenter(GeoPoint center) {
        this.center = center;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
