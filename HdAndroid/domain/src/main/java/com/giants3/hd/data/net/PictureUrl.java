package com.giants3.hd.data.net;

import com.giants3.hd.utils.StringUtils;

/**
 * Created by davidleen29 on 2018/9/13.
 */

public class PictureUrl {


    public static String completeUrl(String url) {
        if (StringUtils.isEmpty(url)) return "";
        if(url.startsWith("http")) return url;


        return  HttpUrl.getBaseUrl()+url;
    }
}
