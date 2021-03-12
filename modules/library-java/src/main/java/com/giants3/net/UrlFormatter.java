package com.giants3.net;

import com.giants3.algorithm.DigestUtils;

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

    private static final void appendParam(StringBuilder url, String param, Object value) {

        url.append( url.indexOf(PARAM_START) >0? PARAM_AND : PARAM_START);

        url.append(param).append(PARAM_EQUAL);

        if (value instanceof String) {

           url.append( encode(value.toString()));

        }else
        {
            url.append(value );
        }



    }


    public static String encode(String value) {
        try {
            return URLEncoder.encode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }

    }

    private static final void signUrl(StringBuilder url) {
        final int beginIndex = url.indexOf(PARAM_START);
        final String paramToSign;
        if (beginIndex > -1)
            paramToSign = url.substring(beginIndex + 1);
        else {
            paramToSign = "";
        }
        //System.out.println("param string: " + paramToSign);
        String sign = DigestUtils.md5(paramToSign);

          appendParam(url, SIGN, sign);

    }


    public static boolean validateSign(String queryString) {


        final String str = SIGN + PARAM_EQUAL;
        int endIndex = queryString.indexOf(str);


        if (endIndex < 0) return false;

        String paramToSign = queryString.substring(0, endIndex - 1);
        String signValue = queryString.substring(endIndex + str.length());

        return DigestUtils.md5(paramToSign).equals(signValue);


    }


    public StringBuilder url;


    public UrlFormatter(String baseUrl) {
        this.url = new StringBuilder(baseUrl);
    }

    public UrlFormatter append(String param, Object value) {
      appendParam(url, param, value);
        return this;
    }


    public String toUrl() {

         signUrl(url);
         return url.toString();
    }
}
