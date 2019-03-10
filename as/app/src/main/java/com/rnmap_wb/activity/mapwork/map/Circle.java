package com.rnmap_wb.activity.mapwork.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.giants3.android.frame.util.Utils;
import com.rnmap_wb.LatLngUtil;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;

public class Circle extends OverlayWithIW {
    private GeoPoint center;
    private boolean clickable;
    private double radiusInMeter;
    int fillColor=Color.parseColor("#33ffff00");
    int innerCircleColor=Color.parseColor("#ec6941");
    Point  point =new Point();
    Paint paint=new Paint();
   public  Circle()
   {

       paint.setAntiAlias(true);
       paint.setDither(true);
   }

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {


        osmv.getProjection().toPixels(center,point);


       // int  v = ((int )radiusInMeter) >> (int )(osmv.getMaxZoomLevel()-osmv.getZoomLevel()) * 256;
        int  v = Utils.dp2px(( int)(  radiusInMeter  /Math.pow(2,(osmv.getMaxZoomLevel()-osmv.getZoomLevel()-2 ) ) * 256));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(Utils.dp2px(2));

        c.drawCircle(point.x,point.y, v,paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(fillColor);
        c.drawCircle(point.x,point.y, v,paint);

         paint.setColor(innerCircleColor);
        c.drawCircle(point.x,point.y, Math.min(Utils.dp2px(4),v/10),paint);



    }

    public void setRadius(double radiusInMeter) {

        this.radiusInMeter = radiusInMeter;
    }

    public void setCenter(GeoPoint center) {
        this.center = center;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
