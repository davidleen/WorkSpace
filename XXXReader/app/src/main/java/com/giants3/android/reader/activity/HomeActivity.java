package com.giants3.android.reader.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.giants3.android.reader.ComicListActivity;
import com.giants3.android.reader.R;
import com.giants3.android.reader.databinding.ActivityHomeBinding;
import com.github.mzule.activityrouter.router.Routers;
import com.nostra13.universalimageloader.core.assist.ws.LibContext;


/**
 * Created by davidleen29 on 2018/12/24.
 */

public class HomeActivity extends  BaseActivity<ActivityHomeBinding> implements View.OnClickListener {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewBinding().comic.setOnClickListener(this);
        getViewBinding().text.setOnClickListener(this);
        getViewBinding().list.setOnClickListener(this);
        LibContext.init(getApplicationContext());

    }

    @Override
    protected ActivityHomeBinding createViewBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.comic: {
                Intent intent = new Intent(HomeActivity.this, ComicListActivity.class);
                startActivity(intent);
            }

                break;
                case R.id.text: {
                    Intent intent = new Intent(HomeActivity.this, TextReadActivity.class);
                    intent.putExtra("filePath", "/sdcard/Download/2222222222222.epub");
                    startActivity(intent);
                }

                break;
//            case R.id.list: {
//
//                        Routers.openForResult(this,"XXX://bookList",999);
//                }
//
//                break;

            case R.id.list: {
                Intent intent = new Intent(HomeActivity.this, BookListActivity.class);
                startActivity(intent);
            }

            break;
        }
    }
}
