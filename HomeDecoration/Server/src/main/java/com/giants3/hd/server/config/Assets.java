package com.giants3.hd.server.config;

import com.giants3.hd.utils.UrlFormatter;

import java.io.File;

/**
 * Created by davidleen29 on 2018/7/4.
 */
public class Assets {
    public   static String ASSETS_URL;

    public static final String PATH_DIVIDER_ON_URL="__";


    public static   String ServerName;

    public static void init(String ip,String serverName)
    {
        ServerName=serverName;
        ASSETS_URL="http://"+ip+":8080/"+serverName+"/api/file/";
    }


    public static String completeUrl(String path)
    {

        return ASSETS_URL+ UrlFormatter.encode(path.replace(File.separator,PATH_DIVIDER_ON_URL));

    }

    public static String urlToPath(String urlPath) {
        return   urlPath.replace(PATH_DIVIDER_ON_URL,File.separator);
    }
}
