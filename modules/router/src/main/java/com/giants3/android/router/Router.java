package com.giants3.android.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

public class Router {
    public static  String SCHEME="rout";

    public static  void open(Context context,String url)
    {

        Uri parse = Uri.parse(url);
        if (SCHEME.equalsIgnoreCase(parse.getScheme())) {
            List<String> pathSegments = parse.getPathSegments();
            StringBuilder stringBuilder=new StringBuilder();
            int size = pathSegments.size();
            for (int i = 0; i < size; i++) {
                stringBuilder.append(pathSegments.get(i));
                if(i<size-1)
                    stringBuilder.append(".");

            }
            String className=stringBuilder.toString();
            Class<?> aClass=null;
            try {
                  aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            }

            if(aClass==null)
            {
                onError(context,url,"no found class for :"+className);
                return;
            }


            Intent intent=new Intent();
            context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);



        }else
        {

        }


    }
    public static  void open(Activity activity,String url,int requestCode)
    {










    }

    public static void onError(Context context,String url,String message)
    {
        if(context.getApplicationContext() instanceof RouterCallBack)
        {
            ((RouterCallBack) context.getApplicationContext()).onFail(context,url,message);
        }
    }
}
