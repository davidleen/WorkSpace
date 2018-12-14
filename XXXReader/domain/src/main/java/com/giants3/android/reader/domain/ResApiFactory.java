package com.giants3.android.reader.domain;

import com.giants3.net.ResApi;

/**
 * Created by davidleen29 on 2018/11/24.
 */

public class ResApiFactory {

    private static ResApiFactory instance;
    public synchronized static ResApiFactory getInstance()
    {

        if(instance==null)
        {

            instance=new ResApiFactory();
        }

        return instance;
    }


    public ResApi getResApi() {
        return resApi;
    }

    public void setResApi(ResApi resApi) {
        this.resApi = resApi;
    }

    private ResApi resApi;


}
