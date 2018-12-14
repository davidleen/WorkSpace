package com.giants3.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by davidleen29 on 2018/2/24.
 */
public class UrlFormatter {


    private static final String PARAM_START = "?";
    private static final String PARAM_AND = "&";
    private static final String PARAM_EQUAL = "=";
    private static final String SIGN = "sign";

    private static final String appendParam(String url, String param, Object value) {

        url += url.contains(PARAM_START) ? PARAM_AND : PARAM_START;


        if (value instanceof String) {

            return url + param + PARAM_EQUAL + encode(value.toString());

        }
        url += param + PARAM_EQUAL + value;
        return url;


    }


    public static String encode(String value) {
        try {
            return URLEncoder.encode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }

    }

    private static final String signUrl(String url) {
        final int beginIndex = url.indexOf(PARAM_START);
        final String paramToSign;
        if (beginIndex > -1)
            paramToSign = url.substring(beginIndex + 1);
        else {
            paramToSign = "";
        }
        //System.out.println("param string: " + paramToSign);
        String sign = DigestUtils.md5(paramToSign);

        return appendParam(url, SIGN, sign);

    }


    public static boolean validateSign(String queryString) {


        final String str = SIGN + PARAM_EQUAL;
        int endIndex = queryString.indexOf(str);


        if (endIndex < 0) return false;

        String paramToSign = queryString.substring(0, endIndex - 1);
        String signValue = queryString.substring(endIndex + str.length());

        return DigestUtils.md5(paramToSign).equals(signValue);


    }


    public String url;


    public UrlFormatter(String baseUrl) {
        this.url = baseUrl;
    }

    public UrlFormatter append(String param, Object value) {
        url = appendParam(url, param, value);
        return this;
    }


    public String toUrl() {

        return signUrl(url);
    }
}
