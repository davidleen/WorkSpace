package com.giants3.hd.sae.api;

import com.giants3.hd.app.AProduct;
import com.giants3.hd.app.AUser;
import com.giants3.hd.sae.entity.WxToken;
import com.giants3.hd.sae.entity.json.WxMixSend;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.exception.HdException;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2016/1/20.
 */
@Component
public class ApiManager {




    public WxToken fetchToken(String appId,String appSecret)throws HdException
    {

        String url= String.format(HttpUrl.URL_GET_TOKEN, appId,appSecret);


        try {
            String result= HttpClientWrapper.getSyncCall(url);
            WxToken wxToken=GsonUtils.fromJson(result,WxToken.class);
            return wxToken;
        }   catch (IOException e) {
            e.printStackTrace();
            throw   HdException.create(e.getMessage());
        }

    }

    public String sendMessage(String message,String appToken) throws HdException {


        String url= String.format(HttpUrl.URL_SEND_MESSAGE,appToken);


        try {
            String result= HttpClientWrapper.postSyncCall(url,message);

            return result;
        }   catch (IOException e) {
            e.printStackTrace();
            throw   HdException.create(e.getMessage());
        }

    }

    public List<AProduct> getProductList(String product) throws HdException {

        Map<String,String> map=new HashMap<>();
        map.put("userName", ConstantData.WX_APP_ID);
        map.put("password",ConstantData.WX_APP_SECRET);
        map.put("client" ,"WEIXIN");
        map.put("version","1.1.0");

        try {
            Type generateType = new TypeToken<RemoteData<AUser>>() {
            }.getType();
            RemoteData<AUser> userRemoteData= GsonUtils.fromJson(  HttpClientWrapper.postSyncCall(HttpUrl.URL_GET_LOGIN,map),generateType);

            if(userRemoteData.isSuccess())
            {
                String url= String.format(HttpUrl.URL_GET_PRODUCT, product,userRemoteData.datas.get(0).token);
                String result= HttpClientWrapper.getSyncCall(url);
                  generateType = new TypeToken<RemoteData<AProduct>>() {
                }.getType();
                RemoteData<AProduct> productRemoteData=GsonUtils.fromJson(result,generateType);
                if(productRemoteData.isSuccess())
                {


                    return productRemoteData.datas;


                }


            }





            return null;



        } catch (IOException e) {
            e.printStackTrace();
            throw HdException.create(e.getLocalizedMessage());
        }

    }
}
