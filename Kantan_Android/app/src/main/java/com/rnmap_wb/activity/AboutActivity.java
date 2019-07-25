package com.rnmap_wb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.SimpleMvpActivity;
import com.rnmap_wb.helper.AndroidUtils;

import butterknife.Bind;

public class AboutActivity extends SimpleMvpActivity {

    @Bind(R.id.version)
    TextView version;

    public static void start(Activity activity) {


        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationController().setTitle("关于我们");
        getNavigationController().setLeftView(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        version.setText("版本:"+AndroidUtils.getVersionName() + "(" + AndroidUtils.getVersionCode() + ")");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_about;
    }

}
