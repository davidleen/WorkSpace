package com.giants3.android.namecardscan;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.giants3.android.api.namecard.NameCard;
import com.giants3.android.api.namecard.NameCardApi;
import com.giants3.android.namecardscan.activity.ShowResultActivity;
import com.intsig.openapilib.OpenApi;
import com.intsig.openapilib.OpenApiParams;

import java.lang.ref.WeakReference;

import static android.app.Activity.RESULT_OK;

/**
 * Created by davidleen29 on 2018/9/10.
 */

public class NameCardApiImpl implements NameCardApi {

    private static final int REQUEST_CODE_RECOGNIZE = 0x1001;
    OpenApi openApi;
    OpenApiParams params = new OpenApiParams() {
        {
            this.setRecognizeLanguage("");
            this.setReturnCropImage(true);
            this.setTrimeEnhanceOption(true);
            this.setSaveCard(false);
        }
    };
    private Activity activity;
    private WeakReference<NameCardPickListener> listenerWeakReference;

    @Override
    public void startScan(Activity activity, NameCardPickListener nameCardPickListener) {

        this.listenerWeakReference = new WeakReference<NameCardPickListener>(nameCardPickListener);


        testRecognizeCapture(activity);





    }


    public NameCardApiImpl() {

        openApi = OpenApi.instance("D1JS5bRBeFEC4dAeDaMUX1HH", "15659169570");


    }


    public void testRecognizeCapture(Activity activity) {
        if (openApi.isCamCardInstalled(activity)) {
            if (openApi.isExistAppSupportOpenApi(activity)) {
                openApi.recognizeCardByCapture(activity, REQUEST_CODE_RECOGNIZE, params);
            } else {
                Toast.makeText(activity, "No app support openapi", Toast.LENGTH_LONG).show();
                System.out.println("camcard download link:" + openApi.getDownloadLink());
            }
        } else {
            Toast.makeText(activity, "No CamCard", Toast.LENGTH_LONG).show();
            System.out.println("camcard download link:" + openApi.getDownloadLink());
        }
    }

    public void testRecognizeImage(Activity activity, String path) {
        if (openApi.isExistAppSupportOpenApi(activity)) {
            openApi.recognizeCardByImage(activity, path, REQUEST_CODE_RECOGNIZE, params);
        } else {
            Toast.makeText(activity, "No app support openapi", Toast.LENGTH_LONG).show();
            System.out.println("camcard download link:" + openApi.getDownloadLink());
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;


        if (requestCode == REQUEST_CODE_RECOGNIZE) {
            String vcf = data.getStringExtra(OpenApi.EXTRA_KEY_VCF);
            String img = data.getStringExtra(OpenApi.EXTRA_KEY_IMAGE);
            showResult(activity, vcf,
                    img);
            Toast.makeText(activity, vcf,
                    Toast.LENGTH_LONG).show();
            if(listenerWeakReference!=null)
            {
                NameCard nameCard=new NameCard();
                nameCard.name="test";
            }
            Log.e("TAG", vcf
                    + img);
        } else {
            int errorCode = data.getIntExtra(openApi.ERROR_CODE, 200);
            String errorMessage = data.getStringExtra(openApi.ERROR_MESSAGE);
            System.out.println("ddebug error " + errorCode + "," + errorMessage);
            Toast.makeText(activity, "Recognize canceled/failed. + ErrorCode " + errorCode + " ErrorMsg " + errorMessage,
                    Toast.LENGTH_LONG).show();
        }

    }


    private void showResult(Activity activity, String vcf, String path) {
        Intent intent = new Intent(activity, ShowResultActivity.class);
        intent.putExtra("result_vcf", vcf);
        intent.putExtra("result_trimed_image", path);
        activity.startActivity(intent);
    }
}
