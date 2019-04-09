package com.giants3.hd.server.service_third;

import com.giants3.hd.domain.api.Client;
import com.giants3.hd.entity.app.PushErrorReport;
import com.giants3.hd.server.noEntity.UmengPushData;
import com.giants3.hd.server.noEntity.UmengPushResult;
import com.giants3.hd.server.repository.PushErrorReportRepository;
import com.giants3.hd.server.service.AbstractService;
import com.giants3.hd.utils.GsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * 推送服务类
 * Created by davidleen29 on 2017/7/21.
 */
@Service
public class PushService extends AbstractService {


    public static final String UMENG_APP_KEY = "57a1e715e0f55a37b8004434";
    public static final String UMENG_APP_MESSAGE_SECRET = "032395580934503e36334cf10bff7b84";
    public static final String UMENG_APP_MASTER_SECRET = "frnbrezlhuqx0bsspyu84nvmshdtm0bt";


    public static final String TYPE_UNICAST = "unicast";
    public static final String TYPE_BROADCAST = "broadcast";

    Client client;

    public static final String URL_PUSH = "http://msg.umeng.com/api/send";
    public static final String URL_PUSH_HTTPS = "https://msgapi.umeng.com/api/send";

    @Autowired
    PushErrorReportRepository pushErrorReportRepository;


    @Override
    public void destroy() throws Exception {
        super.destroy();
        client.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        client = new Client();
    }

    /**
     * 签名文件生成
     */
    private String generateSign(UmengPushData pushData) {


        String method = "POST";
        String url = URL_PUSH_HTTPS;


        String umengPushDataString = GsonUtils.toJson(pushData);
        // String sign = DigestUtils.md5(method + url + umengPushDataString + app_master_secret);

        String sign = null;
        try {
            sign = DigestUtils.md5Hex((method + url + umengPushDataString + UMENG_APP_MASTER_SECRET).getBytes("utf-8"));
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

        } catch (Exception e) {
            e.printStackTrace();
            umengPushResult = new UmengPushResult();
            umengPushResult.ret = UmengPushResult.RET_FAIL;
            umengPushResult.message = e.getMessage();
        }


        return umengPushResult;
    }


    /**
     * 发送 unicast
     */
    public void sendUnicastMessage(String message, String tokens) {


        UmengPushData umengPushData = new UmengPushData();
        umengPushData.type = TYPE_UNICAST;
        umengPushData.device_tokens = tokens;
        umengPushData.payload = new UmengPushData.PushData();
        umengPushData.payload.display_type = UmengPushData.PushData.TYPE_MESSAGE;
        umengPushData.payload.body = new UmengPushData.Data();
        umengPushData.payload.body.custom = message;


        UmengPushResult result = sendMessage(umengPushData);

        switch (result.ret) {

            case UmengPushResult.RET_SUCCESS:
                try {
                    PushErrorReport report = new PushErrorReport();
                    report.code =  UmengPushResult.RET_SUCCESS;
                    report.status =0;
                    report.message =message;
                    pushErrorReportRepository.save(report);
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                break;
            case UmengPushResult.RET_FAIL:


                try {
                    PushErrorReport report = new PushErrorReport();
                    report.code = result.data == null ? "" : result.data.error_code;
                    report.status = result.data == null ? 0 : result.data.status;
                    report.message = result.message;

                    pushErrorReportRepository.save(report);
                } catch (Throwable t) {
                    t.printStackTrace();
                }


                break;


        }
    }


    /**
     * 发送 广播  所有人都接收
     */
    public void sendBroadcastMessage() {


        UmengPushData umengPushData = new UmengPushData();
        umengPushData.type = TYPE_BROADCAST;
        umengPushData.payload = new UmengPushData.PushData();
        umengPushData.payload.display_type = UmengPushData.PushData.TYPE_NOTIFICATION;
        umengPushData.payload.body = new UmengPushData.Data();
        umengPushData.payload.body.ticker = "测试提示文字";
        umengPushData.payload.body.title = "测试标题";
        umengPushData.payload.body.text = "测试文字描述";
        umengPushData.payload.body.after_open = "go_app";
        umengPushData.description = "测试列播通知-Android";


        UmengPushResult result = sendMessage(umengPushData);
        if (result != null)
            switch (result.ret) {

                case UmengPushResult.RET_SUCCESS:
                    break;
                case UmengPushResult.RET_FAIL:
                    break;


            }
    }
}
