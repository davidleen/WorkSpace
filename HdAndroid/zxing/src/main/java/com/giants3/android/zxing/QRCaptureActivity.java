package com.giants3.android.zxing;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.journeyapps.barcodescanner.CaptureActivity;

/**
 * Created by david on 2015/12/19.
 */
public class QRCaptureActivity extends CaptureActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FrameLayout content = (FrameLayout) findViewById(android.R.id.content);
//
//        int widthPixels = getResources().getDisplayMetrics().widthPixels;
//
//        int topPading = (getResources().getDisplayMetrics().heightPixels-widthPixels/2)/2;
//        if(content!=null) {
//            content.setPadding(widthPixels/4, topPading, widthPixels/4, topPading);
//        }
    }
}
