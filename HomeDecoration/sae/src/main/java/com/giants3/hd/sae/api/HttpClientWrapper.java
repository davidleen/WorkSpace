package com.giants3.hd.sae.api;

import com.giants3.hd.utils.GsonUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by david on 2016/1/20.
 */
public class HttpClientWrapper {


    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";


    private static  OkHttpClient okHttpClient = createClient();

    static
    {

    }








    public static String postSyncCall(String url,Object data) throws IOException{

        RequestBody body= RequestBody.create(MediaType.parse(CONTENT_TYPE_VALUE_JSON), GsonUtils.toJson(data));
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON)
                .post(body)
                .build();


        return   okHttpClient.newCall(request).execute().body().string();


    }
    public static String    getSyncCall(String url) throws IOException {

        final Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON)

                .get()
                .build();


           return  okHttpClient.newCall(request).execute().body().string();

    }

    private static OkHttpClient createClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);

        return okHttpClient;
    }


}
