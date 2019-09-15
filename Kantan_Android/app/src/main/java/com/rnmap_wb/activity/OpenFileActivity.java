package com.rnmap_wb.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.giants3.android.FileProviderHelper;
import com.giants3.android.frame.util.Log;
import com.rnmap_wb.activity.mapwork.MapWorkActivity;
import com.rnmap_wb.activity.mapwork.SimpleMvpActivity;

import java.io.File;

/**
 * 各種文件入口处理。
 * Created by davidleen29 on 2018/12/24.
 */

public class OpenFileActivity extends Activity {
    public static final String RELATIVE_PATH_TEMPORARY = "temp/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();


        switch (action) {
            case Intent.ACTION_VIEW:
                Uri uri = intent.getData();
//            File file = new File(URI.create(intent.getDataString()));
                String filePath = null;
                String strUri = null;

                String uri1 = intent.getStringExtra("uri");
                if (TextUtils.isEmpty(uri1)) {
                    if (uri == null) {
                        finish();
                        return;
                    }
                }
                if (!TextUtils.isEmpty(uri1)) {
                    uri = Uri.fromFile(new File(uri1));
                }

                strUri = uri.toString();


                if (URLUtil.isFileUrl(strUri)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        filePath = intent.getStringExtra("uri");
                        if (filePath == null) {  //未得到路径时候，增加解析，容错
                            filePath = uri.getPath();
                        }
                    } else {
                        filePath = uri.getPath();
                    }
                } else if (URLUtil.isNetworkUrl(strUri)) {
                    int index = strUri.lastIndexOf("/") + 1;
                    String str = index > 0 ? strUri.substring(index) : "";

//                    filePath = StorageUtils.getAbsolutePathIgnoreExist(FileBrowser.RELATIVE_PATH_TEMPORARY + str);
//                    ResultMessage result = DownloadHelper.getDownloadUtils(HttpType.get).download(strUri, filePath, -1);
//                    if (result != null && result.getResult() != ResultMessage.RESULT_SUCCESS) {
//                        filePath = null;
//                        Toast.makeText(this, getString(R.string.common_message_fileReadError, strUri), Toast.LENGTH_SHORT).show();
//                    }
                } else if (URLUtil.isContentUrl(strUri)) {


                        File file = FileProviderHelper.getFileFormUri(OpenFileActivity.this, uri);

                        if (file != null) {
                            filePath = file.getPath();

                            MapWorkActivity.start(this, file.getPath());
                            finish();
                        }



                }
                break;
        }
    }

}
