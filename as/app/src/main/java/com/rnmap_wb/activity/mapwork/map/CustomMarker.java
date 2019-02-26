package com.rnmap_wb.activity.mapwork.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rnmap_wb.R;
import com.rnmap_wb.helper.ImageLoaderFactory;
import com.rnmap_wb.map.CustomClusterManager;

import org.osmdroid.bonuspack.kml.IconStyle;
import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.views.MapView;

public class CustomMarker extends org.osmdroid.views.overlay.Marker {

    public IconStyle iconStyle;
     ;

    private Context context;
    public CustomMarker(MapView mapView) {
        super(mapView);
        context=mapView.getContext();
    }

    public CustomMarker(MapView mapView, Context resourceProxy) {
        super(mapView, resourceProxy);
        context=resourceProxy;
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


    private void setCustomIcon(Drawable drawable) {
        setIcon(drawable);


        if (iconStyle!=null) {
            setAnchor(iconStyle.mHotSpot.getX(drawable.getIntrinsicWidth()) * iconStyle.mScale,
                    (1.0f - iconStyle.mHotSpot.getY(drawable.getIntrinsicHeight()) * iconStyle.mScale));

            //Y coords are top->bottom for Marker Anchor, and bottom->up for KML hotSpot
            setRotation(iconStyle.mHeading);
        }


    }


    public void setCustomDefaultIcon() {

        setIcon(context.getResources().getDrawable(R.drawable.icon_map_mark));

    }

    private void loadIcon(final IconStyle iconStyle) {
        ImageLoaderFactory.getInstance().loadImage(iconStyle.mHref, new SimpleImageLoadingListener() {


            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {


                setCustomDefaultIcon();

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                if (imageUri.equals(getIconUrl())) {
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
