package com.giants3.report;

/**
 * Created by davidleen29 on 2018/2/17.
 */
public class PictureUrl {

    // public  static     String BaseUrl="http://192.168.10.198:8080/Server/";
    private static String BaseUrl = "http://127.0.0.1:8080/";
    public static final String completeUrl(String url)
    {

        if(url.startsWith("http://")) return url;

        return BaseUrl+url;







    }

    public static final void  setBaseUrl(String barurl)
    {
        BaseUrl=barurl;
    }
}
