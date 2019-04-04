package com.rnmap_wb.utils;

public class SettingContent {

    private static  SettingContent settingContent=new SettingContent();
    private String token;

    public static  SettingContent getInstance()
    {
        return settingContent;
    }


    public boolean autoDownloadOnWifi()
    {
        return true;
    }

    public void setDeviceToken(String token) {

        this.token = token;
    }
    public String getToken()
    {return token;}
}
