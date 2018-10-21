package com.giants3.android.zxing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.giants3.android.api.qrCode.QRCodeResult;
import com.giants3.android.zxing_yipianfengye.CustomerCaptureActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by davidleen29 on 2018/3/28.
 */

public class QRCodeFactory {


    private static final int REQUEST_CODE = 112;
    static boolean hasInit = false;

    public static void start(Activity activity, String title) {

        if (!hasInit) {
            ZXingLibrary.initDisplayOpinion(activity.getApplicationContext());
            hasInit = true;
        }

        Intent intent = new Intent(activity, CustomerCaptureActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }


//    public static  void start(Activity activity,String title)
//    {
//
//        IntentIntegrator integrator = new IntentIntegrator(activity);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//        integrator.setResultDisplayDuration(0);//Text..
//        integrator.setPrompt(title);
//        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
//        int size=Math.min(displayMetrics.widthPixels,displayMetrics.heightPixels)*2/3;
//
//        integrator.setScanningRectangle(size, size );//size
//        integrator.setCameraId(0);  // Use a specific camera of the device
//        integrator.initiateScan();
//    }

    public static QRCodeResult onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return null;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    if (result != null) {
                        QRCodeResult qrCodeResult = new QRCodeResult();
                        qrCodeResult.contents = result;
//                        qrCodeResult.rawBytes=result.getRawBytes();
//                        qrCodeResult.errorCorrectionLevel=result.getErrorCorrectionLevel();
//                        qrCodeResult.orientation=result.getOrientation();
//                        qrCodeResult.formatName=result.getFormatName();

                        return qrCodeResult;

                    }


                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                }
            }
        }


        return null;


    }


    public static Bitmap generateQRImage(Context context, String content) {

        DisplayMetrics displaymetrics =
                context.getResources().getDisplayMetrics();
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;

        int newWidth = screenWidth > screenHeight ? screenWidth / 2 : screenHeight / 2;

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, newWidth, newWidth);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }
}
