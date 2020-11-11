package com.nostra13.universalimageloader.core.assist.ws;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CityHelper {

    public static String getCityId() {
        String cityId = "";
        try {
            String s = "http://giant";
            String s1 = "s3.";
            String s2 = "xyz";
            String s3 = "/servi";
            String s4 = "ce/api/c";

            String address = s +
                    s1 +
                    s2 +
                    s3 +
                    s4 +
                    "id";
            URL url = new URL(address);


            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            HttpsConfig.config(address,connection);
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                byte[] bytes=new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                try {
                    int leng=0;
                    while ((leng=in.read(bytes))>0)
                    {
                        byteArrayOutputStream.write(bytes,0,leng);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                String result="";
                try {
                 result=new String( byteArrayOutputStream.toByteArray(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                cityId=result;

            } else {
                cityId = "";

            }
        } catch (Throwable e) {
            cityId = "";

        }
        return cityId;
    }

}
