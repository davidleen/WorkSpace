/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.giants3.android.network;


import com.giants3.android.frame.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

//import  okhttp3.MultipartBuilder;

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link java.util.concurrent.Callable} so when executed asynchronously can
 * return a value.
 */

public class ApiConnection implements ResApi {
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String HEAD = "HEAD";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    MediaType MEDIA_TYPE_IMG = MediaType.parse("image/png");
    MediaType MEDIA_TYPE_FILE = MediaType.parse("file/*");
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    private static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";
    //private static final String CONTENT_TYPE_VALUE = CONTENT_TYPE_VALUE_JSON;

    private static final MediaType mediaType = MediaType.parse(CONTENT_TYPE_VALUE);
    private static final MediaType mediaType_image = MediaType.parse("multipart/form-data");

    public static final String DEFAULT_CHAR_ENCODE = "UTF-8";
    private static final String TAG = "ApiConnection";
    private OkHttpClient okHttpClient = createClient();

    Map<String, String> headers = new HashMap<>();
    ;

    static {

    }


    public ApiConnection() {
    }


    @Override
    public byte[] post(String url, byte[] data) throws IOException {


        return request(POST, url, RequestBody.create(mediaType, data)).bytes();
    }

    private ResponseBody request(String method, String url, RequestBody body) throws IOException {

        Request request = null;
        try {
            Request.Builder builder = new Request.Builder()
                    .url(new URL(url));
            builder.addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON);
            builder.headers(Headers.of(headers));
            switch (method) {
                case PUT:

                    builder.put(body);
                    break;

                case POST:
                    builder.post(body);
                    break;
                case GET:

                    builder.get();
                    break;
                case DELETE:
                    builder.delete(body);
                    break;
                case HEAD:
                    builder.head();
                    break;
            }

            request = builder.build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IOException("url:" + url + ",is not a valid url");
        }

        Response res = okHttpClient.newCall(request).execute();
        if(res.isSuccessful())
        {
            return res.body();
        }
        throw
                 new IOException(res.code()+res.message());


    }

    @Override
    public byte[] put(String url, byte[] data) throws IOException {


        return request(PUT, url, RequestBody.create(mediaType, data)).bytes();


    }


    /**
     * 提交二进制流数据
     */
    @Override
    public String postBytes(String url, byte[] data) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse(APPLICATION_OCTET_STREAM), data);
        return new String(request(POST, url, body).bytes(), DEFAULT_CHAR_ENCODE);
    }

    /**
     * 上传文件
     *
     * @param serverURL
     * @param files
     * @return
     * @throws IOException
     */
    @Override
    public String uploadFile(String serverURL, File[] files) throws IOException {


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (File file : files) {
            builder.addFormDataPart("image", file.getName(),
                    RequestBody.create(MEDIA_TYPE_IMG, file));
        }
        RequestBody requestBody = builder
                .build();

        ResponseBody post = request(POST, serverURL, requestBody);

        return new String(post.bytes(), DEFAULT_CHAR_ENCODE);


    }

    /**
     * 上传文件
     *
     * @param serverURL
     * @param  serverURL
     * @return
     * @throws IOException
     */
    @Override
    public String uploadFile(String serverURL, String fieldName, File file) throws IOException {


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart(fieldName, file.getName(),
                RequestBody.create(MEDIA_TYPE_IMG, file));

        RequestBody requestBody = builder
                .build();

        ResponseBody post = request(POST, serverURL, requestBody);

        return new String(post.bytes(), DEFAULT_CHAR_ENCODE);


    }

    @Override
    public String postJson(String url, String data) throws IOException {
        try {
            RequestBody body = RequestBody.create(MediaType.parse(CONTENT_TYPE_VALUE_JSON), data);
            return new String(request(POST, url, body).bytes(), DEFAULT_CHAR_ENCODE);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

    }

    @Override
    public String post(String url, String data) throws IOException {


        try {
            RequestBody body = RequestBody.create(MediaType.parse(CONTENT_TYPE_VALUE), data);
            return new String(request(POST, url, body).bytes(), DEFAULT_CHAR_ENCODE);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

    }

    @Override
    public byte[] get(String url) throws IOException {


        return getResponseBody(url).bytes();


    }


    private ResponseBody getResponseBody(String url) throws IOException {

        return request(GET, url, null);


    }


    @Override
    public InputStream openStream(String url) throws IOException {


        return getResponseBody(url).byteStream();


    }


    /**
     * @param url
     * @return
     * @throws IOException
     */
    @Override
    public String getString(String url) throws IOException {

        return new String(get(url), DEFAULT_CHAR_ENCODE);


    }

    @Override
    public boolean download(String url, String filePath) throws IOException {

        return download(url,filePath,null);

    }
    @Override
    public boolean download(String url, String filePath, final com.giants3.android.network.ProgressListener listener) throws IOException {
        FileUtils.makeDirs(filePath);

        String tempFilePath = filePath + "_temp";
        ResponseBody body = getResponseBody(url);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {


            final long contentLength = body.contentLength();
            inputStream = body.byteStream();

            outputStream = new FileOutputStream(tempFilePath);
            FileUtils.copyStream(inputStream, outputStream, new com.giants3.android.network.ProgressListener() {
                @Override
                public void onProgressUpdate(long current, long totalLength) {
                    listener.onProgressUpdate(current,contentLength);
                }
            });
            outputStream.flush();
        } catch (Throwable t) {
        } finally {
            FileUtils.safeClose(outputStream);
            FileUtils.safeClose(inputStream);
        }

        File tempFile = new File(tempFilePath);
        if (tempFile.exists()) {
            tempFile.renameTo(new File(filePath));
        }


        body.close();


        return new File(filePath).exists();
    }

    private static OkHttpClient createClient() {
        final OkHttpClient okHttpClient

                = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
//        okHttpClient.(10000, TimeUnit.MILLISECONDS);
//        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
//        try {
//            // Copy to customize OkHttp for this request.
//            OkHttpClient copy = okHttpClient.newBuilder()
//                    .readTimeout(500, TimeUnit.MILLISECONDS)
//                    .build();
//
//            Response response = copy.newCall(request).execute();
//            System.out.println("Response 1 succeeded: " + response);
//        } catch (IOException e) {
//            System.out.println("Response 1 failed: " + e);
//        }
        return okHttpClient;
    }


    @Override
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }
}
