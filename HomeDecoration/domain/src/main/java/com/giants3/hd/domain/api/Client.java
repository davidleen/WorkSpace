package com.giants3.hd.domain.api;

import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.crypt.CryptUtils;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ning.http.client.*;
import com.ning.http.client.multipart.FilePart;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * 提供远程请求的客户端类
 */
@Singleton
public class Client {


    public static final String BODY_ENCODING = "UTF-8";
    private static final String TAG = "HTTPCLIENT";
    //客户端连接
    public AsyncHttpClient client;

    @Inject
    public Client() {//设置链接参数   默认超时时间6秒
        client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setConnectTimeout(3000).setRequestTimeout(Integer.MAX_VALUE).setReadTimeout(Integer.MAX_VALUE).build());
    }

    @Inject
    MimetypesFileTypeMap mimetypesFileTypeMap;


    public void put() {
    }


    public String postWithStringReturned(String url, String body) throws HdException {
        Logger.getLogger(TAG).info(url);

        String result = post(url, body, new AsyncCompletionHandler<String>() {
            @Override
            public String onCompleted(Response response) throws Exception {

//                    String result= response.getResponseBody(BODY_ENCODING);

                String result = descryptResult(response.getResponseBodyAsBytes(), BODY_ENCODING);
                return result;


            }
        });


        Logger.getLogger(TAG).info(result);


        return result;

    }


    public String getWithStringReturned(String url) throws HdException {

        Logger.getLogger(TAG).info(url);

        String result = get(url, new AsyncCompletionHandler<String>() {
            @Override
            public String onCompleted(Response response) throws Exception {

                //String result = response.getResponseBody(BODY_ENCODING);


                String result = descryptResult(response.getResponseBodyAsBytes(), BODY_ENCODING);


                return result;


            }
        });


        Logger.getLogger(TAG).info(result);

        return result;
    }

    public InputStream openInputStream(String url) throws HdException {

        Logger.getLogger(TAG).info(url);

        InputStream result = get(url, new AsyncCompletionHandler<InputStream>() {
            @Override
            public InputStream onCompleted(Response response) throws Exception {

                //String result = response.getResponseBody(BODY_ENCODING);


                InputStream result = response.getResponseBodyAsStream();


                return result;


            }
        });




        return result;
    }




    /**
     * 对返回的数据进行 解密
     *
     * @param data
     * @return
     */
    public String descryptResult(byte[] data, String encode) throws IOException {


        data = ConstantData.IS_CRYPT_JSON ? CryptUtils.decryptDES(data, ConstantData.DES_KEY) : data;
        try {
            return new String(data, encode);
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }


    /**
     * 上传文件
     *
     * @param url
     * @param file
     * @return
     */
    public String uploadWidthStringReturned(String url, File file) throws HdException {
        Logger.getLogger(TAG).info(url);
        String result = upload(url, file, new AsyncCompletionHandler<String>() {
            @Override
            public String onCompleted(Response response) throws Exception {

//                String result = response.getResponseBody(BODY_ENCODING);

                String result = descryptResult(response.getResponseBodyAsBytes(), BODY_ENCODING);
                return result;


            }
        });

        Logger.getLogger(TAG).info(result);

        return result;

    }


    public <T> T get(String url, AsyncHandler<T> handler) throws HdException {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.prepareGet(url);
        builder.addHeader("Content-Type", "application/json");
        return execute(handler, builder);
    }

    public <T> T post(String url, String body, AsyncHandler<T> handler) throws HdException {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        builder.addHeader("Content-Type", "application/json");
        builder.setBodyEncoding(BODY_ENCODING);
        byte[] data = null;
        try {
            if (!StringUtils.isEmpty(body))
                data = body.getBytes(BODY_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (data != null && ConstantData.IS_CRYPT_JSON) {

            data = CryptUtils.encryptDES(data, ConstantData.DES_KEY);

        }


        if (null != data)
            builder.setBody(data);

        return execute(handler, builder);

    }


    /**
     * 上传文件
     *
     * @param url
     * @param file
     * @param handler
     * @param <T>
     * @return
     * @throws HdException
     */
    public <T> T upload(String url, File file, AsyncHandler<T> handler) throws HdException {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        builder.addHeader("Content-Type", "multipart/*");
        builder.addBodyPart(new FilePart("file", file, mimetypesFileTypeMap.getContentType(file)));

        return execute(handler, builder);

    }

    private <T> T execute(AsyncHandler<T> handler, AsyncHttpClient.BoundRequestBuilder builder) throws HdException {
        try {

            long time= Calendar.getInstance().getTimeInMillis();
            T result = builder.execute(handler).get();


            //特殊处理  避免请求结果太快返回，
            if(Calendar.getInstance().getTimeInMillis()-time<500)
            {
                try {
                    Thread.sleep(500);
                }catch (Throwable t){}
            }


            Logger.getLogger(TAG).info("time user in :"+ ( Calendar.getInstance().getTimeInMillis()-time)+"in " +Thread.currentThread());

            return result
                    ;
        } catch (InterruptedException e) {
            throw HdException.create(HdException.FAIL_ASYNC_CLIENT, e);
        } catch (ExecutionException e) {
            Throwable cause
                    = e.getCause();
            if (cause != null && cause instanceof HdException) {
                throw (HdException) cause;
            } else
                throw HdException.create(HdException.FAIL_ASYNC_CLIENT, e);

        }
    }

    public void get() {
    }

    public void delete() {

    }


}
