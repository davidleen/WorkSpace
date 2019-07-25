package com.rnmap_wb.activity.mapwork.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;

import com.giants3.android.frame.util.Utils;
import com.rnmap_wb.MainApplication;
import com.rnmap_wb.R;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class CustomPolyline extends Polyline {


    private Paint paint;

    protected Drawable drawable_point;
    protected Drawable point_end;
    private OnMapItemLongClickListener listener;
    private BoundingBox boundingBox;

    public CustomPolyline() {
        drawable_point = ContextCompat.getDrawable(MainApplication.baseContext, R.drawable.icon_map_point);
        point_end = ContextCompat.getDrawable(MainApplication.baseContext, R.drawable.icon_map_point_end);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(Utils.dp2px(2));
        paint.setAntiAlias(true);


    }

    private Point point = new Point();

    //自定义绘制
    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {

        if (getPoints() == null) return;
        if (shadow) {
            return;
        }

        drawable_point.setBounds(0, 0, drawable_point.getIntrinsicWidth(), drawable_point.getIntrinsicHeight());
        point_end.setBounds(0, 0, point_end.getIntrinsicWidth(), point_end.getIntrinsicHeight());
        int lastX = 0, lastY = 0;
        Drawable temp;
        List<GeoPoint> geoPoints = getPoints();
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

                drawLine(c, lastX, lastY, point.x, point.y, paint, i);

            }


            lastX = point.x;
            lastY = point.y;

        }


    }

    @Override
    public void setPoints(List<GeoPoint> points) {
        super.setPoints(points);
       boundingBox= BoundingBox.fromGeoPoints(points);
    }

    protected void drawLine(Canvas c, float startX, float startY, float endX, float endY, Paint paint, int index) {

        c.drawLine(startX, startY, endX, endY, paint);
    }


    @Override
    public boolean onLongPress(MotionEvent e, MapView mapView) {



        GeoPoint geoPoint= (GeoPoint) mapView.getProjection().fromPixels((int)e.getX(),(int )e.getY());
        if(listener!=null&&boundingBox!=null&&boundingBox.contains(geoPoint))
        {
            listener.onLongClick(this);
            return true;
        }
        return super.onLongPress(e, mapView);
    }


    public void setOnLongClickListener(OnMapItemLongClickListener listener)
    {
        this.listener = listener;
    }
}
