package com.giants3.android.api.namecard;

import android.app.Activity;
import android.content.Intent;

import com.giants3.android.api.qrCode.QRCodeResult;

/**
 * Created by davidleen29 on 2018/9/9.
 */

public interface NameCardApi {


    void  startScan(Activity activity, NameCardPickListener listener);

    void onActivityResult(Activity activity,int requestCode, int resultCode, Intent data );



     interface  NameCardPickListener
     {
         void onNameCardPick(NameCard nameCard);
     }
}

