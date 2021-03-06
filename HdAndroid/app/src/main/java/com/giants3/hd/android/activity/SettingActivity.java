package com.giants3.hd.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.giants3.hd.android.BuildConfig;
import com.giants3.hd.android.R;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.app.AUser;

import butterknife.BindView;

//import com.giants3.hd.data.net.HttpUrl;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActionBarActivity {


    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.et_port)
    EditText etPort;
    @BindView(R.id.et_service)
    EditText etService;
    @BindView(R.id.btn_save)
    Button btnSave;

    @BindView(R.id.setOutUrl)
    Button setOutUrl;

    @BindView(R.id.setInUrl)
    Button setInUrl;


    @BindView(R.id.setProduceUrl)
    Button setProduceUrl;

    @BindView(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("网络设置");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSetting();


            }
        });

//        setOutUrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                etIp.setText("fzyunfei.f3322.net");
//                etPort.setText("8079");
//                etService.setText("Server");
//            }
//        });

        setOutUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etIp.setText("fzyunfei.gnway.cc");
                etPort.setText("8079");
                etService.setText("Server");
            }
        });

        setInUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIp.setText("192.168.10.198");
                etPort.setText("8080");
                etService.setText("Server");
            }
        });

        setProduceUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIp.setText("192.168.0.198");
                etPort.setText("8080");
                etService.setText("Server");

            }
        });

        AUser loginUser = SharedPreferencesHelper.getLoginUser();
        setOutUrl.setVisibility(( BuildConfig.DEBUG ||(loginUser!=null&&loginUser.internet))? View.VISIBLE: View.GONE );
        etIp.setText(HttpUrl.IPAddress);
        etPort.setText(HttpUrl.IPPort);
        etService.setText(HttpUrl.ServiceName);

        PackageManager pm = getPackageManager();//context为当前Activity上下文
        PackageInfo pi = null;

        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
            String versionCode = String.valueOf(pi.versionCode);
            String versionName = pi.versionName;
            version.setText("版本号:" + versionCode + ",版本名称:" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.activity_setting, null);
    }


    public void saveSetting() {
        HttpUrl.reset(etIp.getText().toString().trim(), etPort.getText().toString().trim(), etService.getText().toString().trim());

        setResult(RESULT_OK);
        finish();
    }


    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent, requestCode);

    }
}
