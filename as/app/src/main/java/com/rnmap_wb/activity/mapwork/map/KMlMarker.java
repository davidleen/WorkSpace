package com.rnmap_wb.activity.mapwork.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giants3.android.frame.util.StringUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rnmap_wb.R;
import com.rnmap_wb.helper.ImageLoaderFactory;
import com.rnmap_wb.map.CustomClusterManager;

import org.osmdroid.bonuspack.kml.IconStyle;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import butterknife.Bind;

public class KMlMarker extends org.osmdroid.views.overlay.Marker {

    public IconStyle iconStyle;
    ViewHolder holder;


    private Context context;

    public KMlMarker(MapView mapView) {
        super(mapView);
        context = mapView.getContext();
        holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_kml_marker_info, null));
    }

    public KMlMarker(MapView mapView, Context resourceProxy) {
        super(mapView, resourceProxy);
        context = resourceProxy;
    }


    public static class ViewHolder extends AbsViewHolder<Marker> {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.memo)
        TextView memo;


        public ViewHolder(View v) {
            super(v);

        }

        public void bindData(Marker marker) {

            name.setText(marker.getTitle());

            boolean empty = StringUtil.isEmpty(marker.getSnippet());
            memo.setVisibility(!empty ? View.VISIBLE : View.GONE);
            if (!empty) {
                memo.setText(marker.getSnippet());
            }
            memo.setVisibility(View.GONE);


        }


    }

    public void applyIconStyle(IconStyle iconStyle) {


        if (this.iconStyle == iconStyle) return;
        this.iconStyle = iconStyle;


        if (iconStyle == null) {


            if (CustomClusterManager.CLUSTER.equals(getSnippet())) {
                setCustomDefaultIcon();
            } else {
                setCustomDefaultIcon();
            }


        } else {
            setCustomDefaultIcon();
            loadIcon(iconStyle);
        }


    }
    public void bindData() {

        holder.bindData(this);
        //绘制

        View v = holder.getRoot();
        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int measuredHeight = v.getMeasuredHeight();
        int measuredWidth = v.getMeasuredWidth();
        v.layout(0, 0, 0 + measuredWidth, 0 + measuredHeight);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {





        super.draw(canvas, mapView, shadow);
        if ( isDisplayed()&&!shadow&&mIcon!=null&&mAlpha!=0) {


            canvas.save();
            int left = mPositionPixels.x-holder.getRoot().getMeasuredWidth()/2;
            int y = mPositionPixels.y+mIcon.getIntrinsicHeight()/2;

            canvas.translate(left, y);

            holder.getRoot().draw(canvas);
            canvas.restore();

        }
    }

    private void setCustomIcon(Drawable drawable) {
        setIcon(drawable);


        if (iconStyle != null) {
            setAnchor(iconStyle.mHotSpot.getX(drawable.getIntrinsicWidth()) * iconStyle.mScale,
                    (1.0f - iconStyle.mHotSpot.getY(drawable.getIntrinsicHeight()) * iconStyle.mScale));

            //Y coords are top->bottom for Marker Anchor, and bottom->up for KML hotSpot
            setRotation(iconStyle.mHeading);
        }


    }


    public void setCustomDefaultIcon() {

        setIcon(context.getResources().getDrawable(R.drawable.icon_map_point));

    }

    private void loadIcon(final IconStyle iconStyle) {
        ImageLoaderFactory.getInstance().loadImage(iconStyle.mHref, new SimpleImageLoadingListener() {


            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {


                setCustomDefaultIcon();

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                if (imageUri != null && imageUri.equals(getIconUrl())) {
//                    int sizeX = Math.round(loadedImage.getWidth() * iconStyle.mScale);
//                    int sizeY = Math.round(loadedImage.getHeight() * iconStyle.mScale);
//
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(loadedImage, sizeX, sizeY, true);

                    BitmapDrawable drawable = new BitmapDrawable(context.getResources(), loadedImage);

                    setCustomIcon(drawable);

                }

            }


        });


    }


    private String getIconUrl() {
        if (iconStyle == null) return null;
        return iconStyle.mHref;
    }
}
