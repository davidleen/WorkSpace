package com.rnmap_wb.activity.mapwork.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.giants3.android.frame.util.Utils;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;

import java.util.ArrayList;
import java.util.List;

/**
 * 标记尺
 */
public class MappingLine extends OverlayWithIW {


    private List<GeoPoint> points;
    private Context ctx;
    Point temp=new Point();
    Paint paint=new Paint();

    float lastX=0;float lastY=0;

    public MappingLine(Context ctx) {
        super( );

        this.ctx = ctx;
        points=new ArrayList<>();

    }

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {

        lastX=0; lastY=0;
        GeoPoint lastPoint=null;
        for (GeoPoint geoPoint:points) {
            osmv.getProjection().toPixels(geoPoint, temp);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(0.5f);
            c.drawCircle(temp.x, temp.y, Utils.dp2px(1), paint);


            if (lastX != 0 || lastY != 0)
            {

                paint.setColor(Color.BLUE);
                c.drawLine(lastX,lastY,temp.x,temp.y,paint);

            }




            lastX=temp.x;
            lastY=temp.y;
            lastPoint=geoPoint;



        }






    }
}
