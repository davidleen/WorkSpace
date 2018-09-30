package com.giants3.hd.sae.api;

/**
 * Created by david on 2016/1/19.
 */
public class HttpUrl {



    public static final String URL_APP_BASE="http://59.56.182.132:8079/Server/";

    public static final String URL_GET_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public static final String URL_SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
    public static final String URL_GET_PRODUCT = URL_APP_BASE+"api/product/appSearch?name=%s&token=%s";
    public static final String URL_GET_LOGIN = URL_APP_BASE+"api/authority/aLogin";
}
