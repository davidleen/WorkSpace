package com.giants3.android.reader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.giants3.android.FileProviderHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.reader.R;
import com.giants3.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

/**
 * 各種文件入口处理。
 * Created by davidleen29 on 2018/12/24.
 */

public class OpenFileActivity extends BaseActivity  {
    public static final String RELATIVE_PATH_TEMPORARY = "temp/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();


        switch (action) {
            case Intent.ACTION_VIEW:
            case Intent.ACTION_MAIN:
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
                    boolean isFileProvider = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//android7.0暂时处理方案


                        File file = FileProviderHelper.getFileFormUri(OpenFileActivity.this, uri);

                        if (file != null) {
                            filePath = file.getPath();
                            isFileProvider = true;
                        }

                    }
                    if (!isFileProvider) {
                        if (uri.getHost().contains("com.android.htmlfileprovider")) {
                            filePath = uri.getPath();
                        } else if (uri.getHost().contains("com.android.email.AttachmentProvider")) {
                            Toast.makeText(this, getString(R.string.common_message_emailReadError, strUri), Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        } else {
                            Log.d("uri:" + uri);
                            String type = getContentResolver().getType(uri);
                            if (type == null) {
                                Toast.makeText(this, getString(R.string.common_message_fileReadError, strUri), Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }

                            Log.d("type:" + type);
//		    		Toast.makeText(this, "type:" + type, Toast.LENGTH_LONG).show();
                            long time = System.currentTimeMillis();
                            if (type.toLowerCase().equals("text/plain")) {
                                filePath = StorageUtils.getFilePath(RELATIVE_PATH_TEMPORARY + time + "temp.txt");
                            } else {
                                type = type.toLowerCase();
                                if (type.contains("bmp")) {
                                    type = "bmp";
                                }
                                int index = type.lastIndexOf("/") + 1;
                                filePath = StorageUtils.getFilePath(RELATIVE_PATH_TEMPORARY + time + "temp." + (index > 0 ? type.substring(index) : type));
                            }


                            File f = new File(filePath);
                            InputStream inputStream = null;
                            FileOutputStream outputStream = null;
                            try {
                                inputStream = getContentResolver().openInputStream(uri);
                                outputStream = new FileOutputStream(f);
                                FileUtils.copyStream(inputStream, outputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                FileUtils.safeClose(inputStream);
                                FileUtils.safeClose(outputStream);
                            }

                        }
                    }

                } else {
                    Toast.makeText(this, getString(R.string.hint_type_other, URLDecoder.decode(strUri)), Toast.LENGTH_LONG).show();
                }

                if (!TextUtils.isEmpty(filePath)) {
                    File file = new File(filePath);
                    Log.d(file.getAbsolutePath());
                    if (file.exists()) {
                        FileBrowser fileBrowserHelper = new FileBrowser(this);
                        try {
                            fileBrowserHelper.openFile(file);
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
        }
    }

    @Override
    protected ViewBinding createViewBinding() {
        return null;
    }
}
