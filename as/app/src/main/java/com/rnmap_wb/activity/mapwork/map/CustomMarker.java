package com.rnmap_wb.activity.mapwork.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.frame.util.StringUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rnmap_wb.R;
import com.rnmap_wb.entity.MapElement;
import com.rnmap_wb.helper.ImageLoaderFactory;
import com.rnmap_wb.map.CustomClusterManager;

import org.osmdroid.bonuspack.kml.IconStyle;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CustomMarker extends org.osmdroid.views.overlay.Marker {

    public IconStyle iconStyle;
    ViewHolder holder;

    private Context context;

    public CustomMarker(MapView mapView) {
        super(mapView);
        context = mapView.getContext();
        holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_marker_info, null));
    }

    public CustomMarker(MapView mapView, Context resourceProxy) {
        super(mapView, resourceProxy);
        context = resourceProxy;
    }


    class ViewHolder extends AbsViewHolder<MapElement> {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.memo)
        TextView memo;
        @Bind(R.id.images)
        View images;


        @Bind(R.id.img1)
        ImageView img1;
        @Bind(R.id.img2)
        ImageView img2;
        @Bind(R.id.img3)
        ImageView img3;
        private ImageView[] imageViews;

        public ViewHolder(View v) {
            super(v);
            imageViews = new ImageView[]{img1, img2, img3};
        }

        public void bindData(MapElement mapElement) {

            name.setText(mapElement.name);

            boolean empty = StringUtil.isEmpty(mapElement.memo);
            memo.setVisibility(!empty ? View.VISIBLE : View.GONE);
            if (!empty) {
                memo.setText(mapElement.memo);
            }
            handleImageLayouts(mapElement);


        }

        private void handleImageLayouts(MapElement mapElement) {


            List<String> urls = new ArrayList<>();
            List<String> localFilePaths = new ArrayList<>();
            String[] pics = mapElement.picture == null ? null : mapElement.picture.split(MapElement.PICTURE_REGEX);
            if (pics != null)
                for (String url : pics)
                    if (!StringUtil.isEmpty(url))
                        urls.add(url);
            String[] paths = mapElement.filePath == null ? null : mapElement.filePath.split(MapElement.PICTURE_REGEX);
            if (paths != null)
                for (String path : paths) {
                    if (!StringUtil.isEmpty(path))
                        localFilePaths.add(path);
                }

            int urlSize = urls.size();
            int filePathSize = localFilePaths.size();
            for (int i = 0; i < imageViews.length; i++) {
                ImageView picture = imageViews[i];

                String url = i < urlSize ? urls.get(i) : null;
                String filePath = i < filePathSize ? localFilePaths.get(i) : null;

                picture.setVisibility(View.VISIBLE);
                if (!StringUtil.isEmpty(url)) {

                    ImageLoaderFactory.getInstance().displayImage(url, picture);
                } else if (!StringUtil.isEmpty(filePath)) {

                    ImageLoaderFactory.getInstance().displayImage(filePath, picture);
                } else {
                    picture.setVisibility(View.GONE);
                }


            }


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

    public void bindData(MapElement mapElement) {

        holder.bindData(mapElement);
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


        canvas.save();
        int left = mPositionPixels.x;
        int y = mPositionPixels.y;

        canvas.translate(left, y);

        holder.getRoot().draw(canvas);
        canvas.restore();


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
