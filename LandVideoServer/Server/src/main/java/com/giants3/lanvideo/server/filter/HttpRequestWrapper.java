package com.giants3.lanvideo.server.filter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * 所有请求封装类，   对于所有远程请求
 */

public class HttpRequestWrapper extends HttpServletRequestWrapper {
    private static  final String TAG="HttpRequestWrapper";
    private String key;
    public HttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
//        String[] strs=super.getParameterValues(name);
//        if(name.equals("token")){
//            for(int i=0;i<strs.length;i++)
////                strs[i]=AESCodec.decrypt(strs[i], key);//AES解密
//        }
//        return strs;
        String[] values=super.getParameterValues(name);

        Logger.getLogger(TAG).info("name:"+name+", value:"+values );
        return values;
    }
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return super.getInputStream();
    }


}