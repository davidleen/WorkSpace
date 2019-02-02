package com.giants3.android.reader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.giants3.android.reader.ComicListActivity;
import com.giants3.android.reader.R;

import butterknife.Bind;

/**
 * Created by davidleen29 on 2018/12/24.
 */

public class HomeActivity extends  BaseActivity implements View.OnClickListener {

    @Bind(R.id.text)
    View text;
    @Bind(R.id.comic)
    View comic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        comic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.comic:
                Intent intent=new Intent(HomeActivity.this, ComicListActivity.class);
                startActivity(intent);

                break;
        }
    }
}
