package com.rnmap_wb;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rnmap_wb.activity.home.HomeActivity;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.activity.mapwork.SimpleMvpActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends SimpleMvpActivity {

    public static final int SPLASH_TIME = 300;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationController().setVisible(false);
        SplashActivityPermissionsDispatcher.doCreateWithPermissionCheck(this, savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.launch_screen;
    }


    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    public void doCreate(final Bundle savedInstanceState) {
        goToHome();
    }
    private void goToHome() {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent = new Intent(SplashActivity.this,  BuildConfig.DEBUG&&false?MapWorkActivity.class :HomeActivity.class);
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


    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
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
