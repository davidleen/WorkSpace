package com.giants3.android.reader.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.giants3.android.reader.R;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by davidleen29 on 2018/12/24.
 */
@RuntimePermissions
public class SplashActivity extends BaseActivity {


    public static final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashActivityPermissionsDispatcher.doCreateWithPermissionCheck(this, savedInstanceState);
    }
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void doCreate(final Bundle savedInstanceState) {
        goToHome();
    }
    private void goToHome() {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_TIME);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onPermissionDenied() {

//        final DialogUtil dialog = new DialogUtil(this, R.string.hite_humoral, R.string.sd_card_tip, R.string.cancel, R.string.next);
//        dialog.setClicklistener(new DialogUtil.ClickListenerInterface() {
//
//            @Override
//            public void doButton2() {
//                dialog.dismiss();
//                SplashActivityPermissionsDispatcher.doCreateWithPermissionCheck(GuideActivity.this, saved);
//            }
//
//            @Override
//            public void doButton1() {
//
//                dialog.dismiss();
//                finish();
//            }
//        });
//
//        dialog.show();


    }
}
