package com.giants3.android.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.giants3.net.UrlFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by davidleen29 on 2018/11/24.
 */

public class HttpUrl {
    public static final String SHARE_FILE = "url_file";
    public static final String CLIENT_TYPE = "ANDROID";
    public static final String DEFAULT_IPAddress = "www.giants3.xyz";
    public static final String DEFAULT_IPPort = "";
    public static final String DEFAULT_ServiceName = "service";
    public static String BASE_URL = "http://127.0.0.1:8080/ReaderServer/";
    private static String versionCode = "111";
    private static String versionName = "";
    public static String token = "";
    public static int apiVer = 1;
    public static String IPAddress = DEFAULT_IPAddress;
    public static String IPPort = DEFAULT_IPPort;
    public static String ServiceName = DEFAULT_ServiceName;
    public static final String BASE_URL_FORMAT = "http://%s:%s/%s/";
    public static String KEY_IPAddress = "_IPAddress";
    public static String KEY_IPPort = "_IPPort";
    public static String KEY_ServiceName = "_ServiceName";
    private static Map<String, Object> defaultParam;

    public static void init(Context context) {

        SharedPreferences sf = context.getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
        String ip = sf.getString(KEY_IPAddress, "");
        if (ip == "") {
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

        Map<String ,Object> defaultParam=new HashMap<>();

        defaultParam.put("appVersion", versionCode);
                defaultParam.put("client", CLIENT_TYPE);
                defaultParam.put("token", token);
                 defaultParam.put("versionName", versionName);
                 defaultParam.put("apiVer", apiVer);

        init(defaultParam);

    }


    public static void init(Map<String,Object> defaultParam)
    {
        HttpUrl.defaultParam = defaultParam;


    }


    public static String additionInfo(UrlFormatter urlFormatter) {


        Set<String> strings = defaultParam.keySet();
        for (String key:strings)
        {
            urlFormatter.append(key, defaultParam.get(key));
        }


        return urlFormatter.toUrl();


    }

    private static final void generateBaseUrl() {
        BASE_URL = String.format(BASE_URL_FORMAT, IPAddress, IPPort, ServiceName);
    }

    public static String additionInfo(String url) {


        return additionInfo(new UrlFormatter(url));


    }

    public static String getComicBookList() {

        String url = BASE_URL + "api/book/listComic";

        UrlFormatter formatter = new UrlFormatter(url);


        return additionInfo(formatter);

    }public static String getBookList() {

        String url = BASE_URL + "api/book/listBooks";

        UrlFormatter formatter = new UrlFormatter(url);
        return additionInfo(formatter);

    }

    public static String getAuthCodes() {

        String url = BASE_URL + "api/authcodes";

        UrlFormatter formatter = new UrlFormatter(url);


        return additionInfo(formatter);

    }



    public static String getBookCategoryInfo(long bookId) {

        String url = BASE_URL + "api/book/findComicChapters";

        UrlFormatter formatter = new UrlFormatter(url);
        formatter.append("bookId", bookId);


        return additionInfo(formatter);


    }

    public static String getChapterList(long bookId) {
        String url = BASE_URL + "api/book/getChapters";

        UrlFormatter formatter = new UrlFormatter(url);
        formatter.append("bookId",bookId);
        return additionInfo(formatter);
    }
}
