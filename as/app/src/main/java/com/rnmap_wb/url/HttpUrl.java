package com.rnmap_wb.url;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.giants3.android.frame.util.StringUtil;

/**
 * Created by davidleen29 on 2018/11/24.
 */

public class HttpUrl {
    public static final String SHARE_FILE = "url_file";
    public static final String CLIENT_TYPE = "ANDROID";
    public static final String DEFAULT_IPAddress = "193.112.141.231";
    public static final String DEFAULT_IPPort = "8380";
    public static final String DEFAULT_ServiceName = "";
    public static String BASE_URL = "http://211.159.164.231:8380/api/";
    private static String versionCode = "111";
    private static String versionName = "";
    public static String token = "";
    public static String IPAddress = DEFAULT_IPAddress;
    public static String IPPort = DEFAULT_IPPort;
    public static String ServiceName = DEFAULT_ServiceName;
    public static final String BASE_URL_FORMAT = "http://%s:%s/%s/";
    public static String KEY_IPAddress = "_IPAddress";
    public static String KEY_IPPort = "_IPPort";
    public static String KEY_ServiceName = "_ServiceName";
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
        SharedPreferences sf = context.getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
        String ip = sf.getString(KEY_IPAddress, "");
        if (StringUtil.isEmpty(ip)) {
            SharedPreferences.Editor text = sf.edit();
            text.putString(KEY_IPAddress, DEFAULT_IPAddress);
            text.putString(KEY_IPPort, DEFAULT_IPPort);
            text.putString(KEY_ServiceName, DEFAULT_ServiceName);
            text.commit();

        }


        IPAddress = sf.getString(KEY_IPAddress, DEFAULT_IPAddress);
        IPPort = sf.getString(KEY_IPPort, DEFAULT_IPPort);
        ServiceName = sf.getString(KEY_ServiceName, DEFAULT_ServiceName);
        generateBaseUrl();
        PackageManager pm = context.getPackageManager();//context为当前Activity上下文
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(pi.versionCode);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }


    public static String additionInfo(UrlFormatter urlFormatter) {


        urlFormatter.append("appVersion", versionCode)
                .append("client", CLIENT_TYPE)
                .append("token", token)
                .append("versionName", versionName);


        return urlFormatter.toUrl();


    }

    private static final void generateBaseUrl() {
        BASE_URL = String.format(BASE_URL_FORMAT, IPAddress, IPPort, ServiceName);
    }

    public static String additionInfo(String url) {

        UrlFormatter urlFormatter = new UrlFormatter(url).append("appVersion", versionCode)
                .append("client", CLIENT_TYPE)
                .append("token", token)
                .append("versionName", versionName);


        return urlFormatter.toUrl();


    }

    public static String getProjectTasks() {

        String url = BASE_URL + "api/project/list";

        UrlFormatter formatter = new UrlFormatter(url);


        return additionInfo(formatter);

    }


    public static String getLogin(String email, String password) {
        String url = BASE_URL + "api/login";

        UrlFormatter formatter = new UrlFormatter(url);
        formatter.append("email", email);
        formatter.append("password", password);
        return additionInfo(formatter);
    }

    public static String register(String email, String code, String password, String password2) {
        String url = BASE_URL + "api/register";

        UrlFormatter formatter = new UrlFormatter(url);
        formatter.append("email", email);
        formatter.append("password", password);
        formatter.append("code", code);
        return additionInfo(formatter);
    }

    public static String getVerifyCode(String email) {
        String url = BASE_URL + "api/register/getCode";

        UrlFormatter formatter = new UrlFormatter(url);
        formatter.append("email", email);
        return additionInfo(formatter);
    }

    public static String getSynchronizeFlags() {

        String url = BASE_URL + "api/project/saveFlag";

        UrlFormatter formatter = new UrlFormatter(url);
        return additionInfo(formatter);
    }

    public static String getUploadFileUrl() {
        String url = BASE_URL + "api/project/uploadImg";

        UrlFormatter formatter = new UrlFormatter(url);
        return additionInfo(formatter);

    }
}
