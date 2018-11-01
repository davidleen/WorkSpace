package com.giants3.android.lan_vedio;

/**
 * Created by davidleen29 on 2018/10/28.
 */

public class HttpUrl {

    public static  final  String BASE_URL="http://192.168.1.6:8080/lanvideo/";
    public static final String complteUrl(String  url)
    {
        return BASE_URL+url;
    }
}
