package com.giants3.hd.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by davidleen29 on 2018/9/16.
 */

public class ServerConfig {


    public String PICTURE_BASE_URL;
    /**
     * 是否支持同步功能（从另外一个服务器复制数据）
     */
    public boolean SYNCHRONISE_DATA;


    public boolean DEBUG;


    public boolean isDebug() {


        return DEBUG;

    }


    public static    ServerConfig config;

    public static ServerConfig getInstance()
    {
        return  config;
    }

    public static void setConfig ( ServerConfig aConfig)
    {
        config=aConfig;
    }

}
