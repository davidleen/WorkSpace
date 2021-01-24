package com.giants3.android.reader.activity;

import android.os.AsyncTask;
import android.util.Log;

import com.giants3.android.frame.util.ToastHelper;
import com.giants3.android.network.ApiConnection;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.io.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.greenrobot.common.io.IoUtils;

public class DownLoadBookTask  extends AsyncTask<Void,Void,Boolean> {


    private final String url;
    private final String path;

    public DownLoadBookTask(String url, String path)
    {

        this.url = url;
        this.path = path;
    }

    @Override
    protected void onPreExecute() {

        ToastHelper.show("正在下载:"+url);
    }
    @Override
    protected Boolean doInBackground(Void[] objects) {
        boolean download=false;
        try {
              download = ResApiFactory.getInstance().getResApi().download(url, path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return download;
    }
}
