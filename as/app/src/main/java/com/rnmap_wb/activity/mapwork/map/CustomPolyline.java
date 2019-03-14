package com.rnmap_wb.activity.mapwork.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.giants3.android.frame.util.Utils;
import com.rnmap_wb.MainApplication;
import com.rnmap_wb.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;

import java.util.List;

public class CustomPolyline extends OverlayWithIW {


    public void setGeoPoints(List<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
    }

    public List<GeoPoint> geoPoints;

    private Paint paint;

    protected  Drawable drawable_point;
    protected  Drawable point_end;
    public CustomPolyline() {
          drawable_point = ContextCompat.getDrawable(MainApplication.baseContext, R.drawable.icon_map_point);
          point_end = ContextCompat.getDrawable(MainApplication.baseContext, R.drawable.icon_map_point_end);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(Utils.dp2px(2));
        paint.setAntiAlias(true);

    }

    private Point point = new Point();

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {

        if (geoPoints == null) return;


        drawable_point.setBounds(0, 0, drawable_point.getIntrinsicWidth(), drawable_point.getIntrinsicHeight());
        point_end.setBounds(0, 0, point_end.getIntrinsicWidth(), point_end.getIntrinsicHeight());
        int lastX = 0, lastY = 0;
        Drawable temp;
        int size = geoPoints.size();
        for (int i = 0; i < size; i++) {
            GeoPoint geoPoint = geoPoints.get(i);
            osmv.getProjection().toPixels(geoPoint, point);
            temp = (i < size - 1 ? drawable_point : point_end);
            c.save();
            c.translate(point.x - temp.getIntrinsicWidth() / 2, point.y - temp.getIntrinsicHeight() / 2);

            temp.draw(c);

            c.restore();

            if (i > 0) {

                drawLine(c,lastX, lastY, point.x, point.y, paint,i);

            }



            lastX = point.x;
            lastY = point.y;

        }


    }


    protected  void drawLine(Canvas c,float startX, float startY,float endX,float endY,Paint paint,int index)
    {

        c.drawLine(startX, startY, endX, endY, paint);
    }
}