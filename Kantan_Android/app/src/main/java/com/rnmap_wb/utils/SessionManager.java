package com.rnmap_wb.utils;
import  android.content.Context;
import android.content.SharedPreferences;

import com.giants3.android.reader.domain.GsonUtils;
import com.rnmap_wb.android.data.LoginResult;

public class SessionManager {

    static  LoginResult loginResult;

    public static final LoginResult getLoginUser(Context context)
    {
        if(loginResult==null)
        {
            loginResult=GsonUtils.fromJson(getShareFile(context).getString("LOGIN_USER",""),LoginResult.class);
        }

        return loginResult;
    }

    private static SharedPreferences getShareFile(Context context) {
        return context.getSharedPreferences("LOGINDATA" ,Context.MODE_PRIVATE);
    }

    public static void saveLoginUser(Context context,LoginResult aResult)
    {

        loginResult=aResult;

        SharedPreferences sharedPreferences=getShareFile(context);
        sharedPreferences.edit().putString("LOGIN_USER",GsonUtils.toJson(loginResult)).apply();



    }
}
