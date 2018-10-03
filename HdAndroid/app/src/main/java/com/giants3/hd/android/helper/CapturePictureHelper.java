package com.giants3.hd.android.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.UriFileComapt;
import com.giants3.hd.android.BuildConfig;
import com.giants3.hd.android.R;
import com.giants3.hd.android.mvp.AndroidRouter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by david on 2016/5/9.
 */
public class CapturePictureHelper implements View.OnClickListener {

    public static final int DEFAULT_OUT_WIDTH = 600;
    public static final int DEFAULT_OUT_HEIGHT = 600;
    public int outWidth = DEFAULT_OUT_WIDTH;
    public int outHeight = DEFAULT_OUT_HEIGHT;
    AndroidRouter activity;


    private View ly_from_album;
    private View ly_from_camera;
    private AlertDialog mAlertDialog;

    OnPictureGetListener listener;

    //是否裁剪
    private boolean clip=false;
    //是否相机获取
    private boolean fromCamera=false;
    //是否相册获取
    private boolean fromAlbum=false;

    public CapturePictureHelper(AndroidRouter activity,  OnPictureGetListener listener) {
        this.activity = activity;
        this.listener = listener;
        initTempFile();
    }

    public final static int FROM_ALBUM = 9911;
    public final static int START_CAMERA = 9912;
    public final static int SET_PIC = 9913;

    private File tempFilePath;


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK) return;

        switch (requestCode) {
            case FROM_ALBUM:
                if (data != null) {

                    if(clip) {


                    // Uri uri=   UriFileComapt.fromFile(activity.getContext(),tempFilePath);
                     Uri uri=   Uri.fromFile( tempFilePath);
                        startPhotoZoom(data.getData(), uri);
                    }else
                    {
                        setPicToView(data);
                    }
                }
                break;

            case START_CAMERA:


                if (tempFilePath.exists()) {

                    if(clip) {
                        Uri uri=   UriFileComapt.fromFile(activity.getContext(),tempFilePath);
                        startPhotoZoom(uri, uri);
                    }else
                    {
                        setPicToView(data);
                    }
                }

                break;

            case SET_PIC:
                if (data != null) {
                    setPicToView(data);
                }
                break;

        }
    }


    /**
     * 将捕捉后的图片显示
     *
     * @param data
     */

    private void setPicToView(Intent data) {



        if((tempFilePath==null||!tempFilePath.exists())&& data.getData()!=null)
        {



            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = activity.getContext().getContentResolver().openInputStream(data.getData());
                outputStream =new FileOutputStream(tempFilePath);
                FileUtils.copyStream(inputStream,outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                FileUtils.safeClose(inputStream);
                FileUtils.safeClose(outputStream);
            }


        }


        if(tempFilePath.exists())
        {




                if (listener != null) {
                    listener.onPictureFileGet(tempFilePath.toString());
                }
                if (tempFilePath != null && tempFilePath.exists()) {
                    tempFilePath.delete();
                }



        }




    }

    /**
     * 从图库中选择
     *
     * @param data
     */
    private void startPhotoZoom(Uri data,Uri dest) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(data, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", outWidth/outHeight);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", outWidth);
            intent.putExtra("outputY", outHeight);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,dest);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(intent, SET_PIC);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.showShort("相册打开失败");
        }
    }


    private void doPick()
    {


        if(!fromAlbum&&!fromCamera)
        {
            throw new RuntimeException("图片获取必须从相册或者相机");
        }
        clearAlertDialog();
        final View pickPanel = LayoutInflater.from(activity.getContext()) .inflate(
                R.layout.dialog_pick_photo, null);
        ly_from_album = pickPanel
                .findViewById(R.id.ly_from_gallery);
        ly_from_camera = pickPanel
                .findViewById(R.id.ly_from_camera);

        ly_from_album.setVisibility(fromAlbum?View.VISIBLE:View.GONE);
        ly_from_camera.setVisibility(fromCamera?View.VISIBLE:View.GONE);

        ly_from_album.setOnClickListener(this);
        ly_from_camera.setOnClickListener(this);
        mAlertDialog = new AlertDialog.Builder(activity.getContext())
                .setTitle("图片选择")
                .setView(pickPanel)
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();

    }

    public void pickPhoto() {
        clip=true;
        fromCamera=true;
        fromAlbum=true;


       doPick();


    }


    /**
     * 相机趣图，
     * @param clip 是否裁剪
     */
    public  void pickFromCamera(boolean clip)
    {


        this.clip=clip;
        fromCamera=true;
        fromAlbum=BuildConfig.DEBUG;
        doPick();

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ly_from_camera:
                clearAlertDialog();
                try {
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Uri uri= UriFileComapt.fromFile(activity.getContext(),tempFilePath);



                   // intent2.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);

                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent2.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 50);
                    activity.startActivityForResult(intent2, START_CAMERA);
                } catch (Exception e) {

                    e.printStackTrace();
                    ToastHelper.showShort("相机打开失败");
                }
                break;
            case R.id.ly_from_gallery:
                clearAlertDialog();
                try {
                    Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                    intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    activity.startActivityForResult(intent1, FROM_ALBUM);
                } catch (Exception e) {

                    ToastHelper.showShort("图库打开失败");
                }
                break;
        }

    }

    private void clearAlertDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    /**
     * 回调接口
     */
    public interface OnPictureGetListener {

        /**
         * 图片文件路径的回调， 这个路径是临时的， 回调结束后会删除。
          * @param filePath
         */
        void onPictureFileGet(String filePath);

    }

    private void initTempFile()
    {
        if(tempFilePath==null||!tempFilePath.exists()) {
            tempFilePath = new File(StorageUtils.getFilePath("temp/temp_picture.jpg"));
            FileUtils.makeDirs(tempFilePath.getAbsolutePath());
        }

    }

    public void setClip(boolean needClip)
    {
        this.clip=needClip;
    }
}
