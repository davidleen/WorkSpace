package com.giants3.reader.server.service;

import com.giants3.reader.domain.api.Client;
import com.giants3.reader.server.noEntity.UmengPushData;
import com.giants3.reader.server.noEntity.UmengPushResult;

import com.giants3.reader.utils.GsonUtils;
import com.giants3.reader.exception.HdException;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * 推送服务类
 * Created by davidleen29 on 2017/7/21.
 */
@Service
public class PushService  extends  AbstractService{


    public static final String UMENG_APP_KEY = "57a1e715e0f55a37b8004434";
    public static final String UMENG_APP_MESSAGE_SECRET = "032395580934503e36334cf10bff7b84";
    public static final String UMENG_APP_MASTER_SECRET = "frnbrezlhuqx0bsspyu84nvmshdtm0bt";


    public static final String  TYPE_UNICAST="unicast";
    public static final String  TYPE_BROADCAST="broadcast";

    Client client ;

    public static final String URL_PUSH = "http://msg.umeng.com/api/send";
    public static final String URL_PUSH_HTTPS = "https://msgapi.umeng.com/api/send";


    @Override
    public void destroy() throws Exception {
        super.destroy();
        client.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        client= new Client();
    }

    /**
     * 签名文件生成
     */
    private String generateSign(UmengPushData pushData) {



        String method = "POST";
        String url = URL_PUSH_HTTPS;


        String umengPushDataString = GsonUtils.toJson(pushData);
       // String sign = DigestUtils.md5(method + url + umengPushDataString + app_master_secret);

        String sign= null;
        try {
            sign = DigestUtils.md5Hex((method + url + umengPushDataString +UMENG_APP_MASTER_SECRET).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;


    }


    private UmengPushResult sendMessage(UmengPushData pushData) {
        UmengPushResult umengPushResult = null;

        String sign = generateSign(pushData);

        String postUrl = URL_PUSH_HTTPS + "?sign=" + sign;
        String umengPushDataString = GsonUtils.toJson(pushData);
        try {
            String result = client.postWithStringReturned(postUrl, umengPushDataString);


            umengPushResult = GsonUtils.fromJson(result, UmengPushResult.class);

        } catch (HdException e) {
            e.printStackTrace();
        }


        return umengPushResult;
    }


    /**
     * 发送 unicast
     */
    public void  sendUnicastMessage(String message, String tokens)
    {


        UmengPushData umengPushData=new UmengPushData();
        umengPushData.type=TYPE_UNICAST;
        umengPushData.device_tokens=tokens;
        umengPushData.payload=new UmengPushData.PushData();
        umengPushData.payload.display_type=UmengPushData.PushData.TYPE_MESSAGE;
        umengPushData.payload.body=new UmengPushData.Data();
        umengPushData.payload.body.custom=message;


       UmengPushResult result= sendMessage(umengPushData);

        switch (result.ret)
        {

            case  UmengPushResult.RET_SUCCESS:break;
            case  UmengPushResult.RET_FAIL:break;


        }
    }


    /**
     * 发送 unicast
     */
    public void  sendBroadcastMessage()
    {


        UmengPushData umengPushData=new UmengPushData();
        umengPushData.type=TYPE_BROADCAST;
        umengPushData.payload=new UmengPushData.PushData();
        umengPushData.payload.display_type=UmengPushData.PushData.TYPE_NOTIFICATION;
        umengPushData.payload.body=new UmengPushData.Data();
        umengPushData.payload.body.ticker="测试提示文字";
        umengPushData.payload.body.title="测试标题";
        umengPushData.payload.body.text="测试文字描述";
        umengPushData.payload.body.after_open="go_app";
        umengPushData.description="测试列播通知-Android";


        UmengPushResult result= sendMessage(umengPushData);
        if(result!=null)
        switch (result.ret)
        {

            case  UmengPushResult.RET_SUCCESS:break;
            case  UmengPushResult.RET_FAIL:break;


        }
    }
}
