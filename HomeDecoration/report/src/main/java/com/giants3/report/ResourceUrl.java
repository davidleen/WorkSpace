package com.giants3.report;

import com.giants3.hd.utils.StringUtils;

/**
 * Created by davidleen29 on 2018/2/17.
 */
public class ResourceUrl {

      private  static     String BaseUrl="http://fzyunfei.f3322.net:8079/Resource/";

    //private static String BaseUrl = "http://127.0.0.1:8080/";
    public static final String completeUrl(String url)
    {


        if(StringUtils.isEmpty(url)) return url;
        if(url.startsWith("http")) return url;

          String s = BaseUrl + url;

        return s;







    }

    public static final void  setBaseUrl(String barurl)
    {
       BaseUrl=barurl;
    }
}
