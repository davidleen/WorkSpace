package com.rnmap_wb.activity.mapwork.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giants3.android.frame.util.StringUtil;
import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.MainApplication;
import com.rnmap_wb.R;
import com.rnmap_wb.entity.MapElement;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MappingPolyline extends CustomPolyline {

    Drawable mappingDrawable;
    ViewHolder holder;
    List<String> informString = new ArrayList<>();
    private int infoType;

    public void setInfoType(int infoType) {

        this.infoType = infoType;
    }

    public int getInfoType() {
        return infoType;
    }

    class ViewHolder extends AbsViewHolder<String> {
        @Bind(R.id.distance)
        TextView distance;

        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(String data) {

            distance.setText(data);
        }
    }

    @Override
    public void setPoints(List<GeoPoint> geoPoints) {
        super.setPoints(geoPoints);

        informString.clear();

        int size = geoPoints.size();
        for (int i = 0; i < size; i++) {

            if (i == 0) {
                informString.add("");
                continue;
            }

            GeoPoint geoPoint = geoPoints.get(i);


            GeoPoint geoPrevious = geoPoints.get(i - 1);
            switch (infoType)
            {
                case MapElement.TYPE_MAPPING_LINE:

                    double distance = LatLngUtil.distanceInMeter(geoPoint, geoPrevious);
                    if (distance > 1000000) informString.add((int) (distance / 1000000) + "公里");
                    else if (distance > 1000) informString.add((int) (distance / 1000) + "千米");
                    else informString.add((int) (distance) + "米");
                    break;
                case MapElement.TYPE_MAPPING_LINE_DEGREE:

                    if(i==size-1)
                        informString.add("");
                    else
                    {
                        GeoPoint next=geoPoints.get(i+1);

//                        关于向量a和b，向量夹角余弦公式
//                        a*b = |a||b|cosTheta
//                        所以
//                                夹角 = acos((a*b )/ (|a|*|b|))
//
//
//                        两向量的数量积 = a.x*b.x + a.y*b.y
//
//                        向量的模 = sqrt(x*x+y*y)
                        double AX=geoPrevious.getLatitude()-geoPoint.getLatitude();
                        double AY=geoPrevious.getLongitude()-geoPoint.getLongitude();
                        double BX=next.getLatitude()-geoPoint.getLatitude();
                        double BY=next.getLongitude()-geoPoint.getLongitude();
                        double degree=Math.toDegrees(Math.acos((AX*BX+AY*BY)/(Math.sqrt(AX*AX+AY*AY)*Math.sqrt(BX*BX+BY*BY))));
                        informString.add(String.format("%.1f°",degree));
                    }


                    break;
            }



        }


    }

    public MappingPolyline() {
        mappingDrawable = ContextCompat.getDrawable(MainApplication.baseContext, R.drawable.icon_line_mapping_tint);
        holder = new ViewHolder(LayoutInflater.from(MainApplication.baseContext).inflate(R.layout.view_mapping_line_info, null));
    }


    @Override
    protected void drawLine(Canvas c, float startX, float startY, float endX, float endY, Paint paint, int index) {


        c.save();

        float distance = (float) Math.sqrt(Math.pow(endY - startY, 2) + Math.pow(endX - startX, 2));
        float degree = (float) Math.atan2(-(endY - startY), endX - startX);
        c.translate(startX, startY);
        c.rotate(-(float) Math.toDegrees(degree));

        //c.rotate((float) , lastX, lastY);
        mappingDrawable.setBounds(0, -mappingDrawable.getIntrinsicHeight() / 2, (int) distance, mappingDrawable.getIntrinsicHeight() / 2);
        mappingDrawable.draw(c);
        c.restore();


        if (informString.size() > index) {
            String data = informString.get(index);
            if (StringUtil.isEmpty(data)) return;

            c.save();
            c.translate(endX + drawable_point.getIntrinsicWidth()/2, endY + drawable_point.getIntrinsicHeight()/2);
            View v = holder.getRoot();

            holder.bindData(data);
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int measuredHeight = v.getMeasuredHeight();
            int measuredWidth = v.getMeasuredWidth();
            v.layout(0, 0, 0 + measuredWidth, 0 + measuredHeight);
            v.draw(c);
            c.restore();
        }

    }
}
