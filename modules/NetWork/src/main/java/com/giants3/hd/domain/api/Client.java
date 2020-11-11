package com.giants3.hd.domain.api;

import com.ning.http.client.*;
import com.ning.http.client.multipart.FilePart;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * 提供远程请求的客户端类
 */

public class Client {


    public static final String BODY_ENCODING = "UTF-8";
    private static final String TAG = "HTTPCLIENT";
    //客户端连接
    public AsyncHttpClient client;


    public Client() {//设置链接参数   默认超时时间6秒
        client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setConnectTimeout(3000).setRequestTimeout(Integer.MAX_VALUE).setReadTimeout(Integer.MAX_VALUE).build());
    }

    MimetypesFileTypeMap mimetypesFileTypeMap;


    public void put() {
    }


    public String postWithStringReturned(String url, String body) throws Exception {
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


    public InputStream openInputStream(String url) throws Exception {
        Logger.getLogger(TAG).info(url);

        InputStream result = get(url, new AsyncCompletionHandler<InputStream>() {
            @Override
            public InputStream onCompleted(Response response) throws Exception {

                InputStream result = response.getResponseBodyAsStream();


                return result;


            }
        });


        return result;


    }
    public String getWithStringReturned(String url) throws Exception
    {
        return getWithStringReturned(url,null);

    }

    public String getWithStringReturned(String url,Map<String,String> headers) throws Exception {

        Logger.getLogger(TAG).info(url);

        String result = get(url, headers,new AsyncCompletionHandler<String>() {
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


    /**
     * 对返回的数据进行 解密
     *
     * @param data
     * @return
     */
    public String descryptResult(byte[] data, String encode) throws IOException {


        return new String(data, encode);

    }


    /**
     * 上传文件
     *
     * @param url
     * @param file
     * @return
     */
    public String uploadWidthStringReturned(String url, File file) throws Exception {
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


    public String post(String url, Map<String, String> heads, String body) throws Exception {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        addHeader(heads,builder);
        builder.setBodyEncoding(BODY_ENCODING);
        builder.setBody(body);
        return execute(new AsyncCompletionHandler<String>() {
            @Override
            public String onCompleted(Response response) throws Exception {
                return response.getResponseBody();
            }
        }, builder);


    }

    private void addHeader(Map<String,String> header,AsyncHttpClient.BoundRequestBuilder builder)
    {
        if(header==null) return;
        Set<String> set = header.keySet();
        for (String key : set) {
            builder.addHeader(key, header.get(key));
        }

    }

    public <T> T get(String url, AsyncHandler<T> handler) throws Exception {


        return get(url,null,handler);
    }

    public <T> T get(String url,Map<String,String> headers, AsyncHandler<T> handler) throws Exception {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.prepareGet(url);
        addHeader(headers,builder);
        builder.addHeader("Content-Type", "application/json");
        return execute(handler, builder);
    }

    public <T> T post(String url, String body, AsyncHandler<T> handler) throws Exception {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        builder.addHeader("Content-Type", "application/json");
        builder.setBodyEncoding(BODY_ENCODING);
        byte[] data = null;
        try {
            if (!isEmpty(body))
                data = body.getBytes(BODY_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if (null != data)
            builder.setBody(data);

        return execute(handler, builder);

    }

    private boolean isEmpty(String data) {
        return data == null || data.trim().equals("");
    }


    /**
     * 上传文件
     *
     * @param url
     * @param file
     * @param handler
     * @param <T>
     * @return
     */
    public <T> T upload(String url, File file, AsyncHandler<T> handler) throws Exception {

        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        builder.addHeader("Content-Type", "multipart/*");
        builder.addBodyPart(new FilePart("file", file, mimetypesFileTypeMap.getContentType(file)));

        return execute(handler, builder);

    }

    private <T> T execute(final AsyncHandler<T> handler, AsyncHttpClient.BoundRequestBuilder builder) throws Exception {
        try {

            long time = Calendar.getInstance().getTimeInMillis();


            AsyncHandler wrapper=new AsyncHandler() {
                @Override
                public void onThrowable(Throwable t) {
                    handler.onThrowable(t);
                }

                @Override
                public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                    return handler.onBodyPartReceived(bodyPart);
                }

                @Override
                public STATE onStatusReceived(HttpResponseStatus responseStatus) throws Exception {

                    if(responseStatus.getStatusCode()!= 200)
                    {
                        throw new Exception(responseStatus.getStatusCode()+","+responseStatus.getStatusText() );
                    }
                    return handler.onStatusReceived(responseStatus);
                }

                @Override
                public STATE onHeadersReceived(HttpResponseHeaders headers) throws Exception {
                    return handler.onHeadersReceived(headers);
                }

                @Override
                public Object onCompleted() throws Exception {
                    return handler.onCompleted();
                }
            };
            ListenableFuture<T> execute = builder.execute(wrapper);
            T result = execute.get();


            //特殊处理  避免请求结果太快返回，
            if (Calendar.getInstance().getTimeInMillis() - time < 500) {
                try {
                    Thread.sleep(500);
                } catch (Throwable t) {
                }
            }


            Logger.getLogger(TAG).info("time user in :" + (Calendar.getInstance().getTimeInMillis() - time) + "in " + Thread.currentThread());

            return result
                    ;
        } catch (InterruptedException e) {
            throw new Exception(e);
        } catch (ExecutionException e) {

            throw new Exception(e);
        }
    }



    public void delete() {

    }


    public void close() {
        client.closeAsynchronously();
    }

}
